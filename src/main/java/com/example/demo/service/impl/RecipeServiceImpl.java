package com.example.demo.service.impl;

import com.example.demo.entity.RecipeEntity;
import com.example.demo.entity.userlist.UserListEntity;
import com.example.demo.exception.list.RecipeListIsBlankException;
import com.example.demo.exception.list.UserListNotFoundException;
import com.example.demo.exception.recipe.RecipeNotFoundException;
import com.example.demo.exception.user.UserNotFoundException;
import com.example.demo.repository.RecipeRepository;
import com.example.demo.service.RecipeService;
import com.example.demo.service.UserService;
import com.example.demo.util.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.demo.repository.UserListRepository;

import java.util.*;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    final RecipeRepository recipeRepository;
    final UserListRepository userListRepository;
    final UserService userService;
    final MapUtils mapUtils;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             UserListRepository userListRepository, MapUtils mapUtils, UserService userService) {
        this.recipeRepository = recipeRepository;
        this.userListRepository = userListRepository;
        this.mapUtils = mapUtils;
        this.userService = userService;
    }

    public RecipeEntity getRandomRecipe() throws RecipeNotFoundException {
        Random random = new Random();
        List<Long> allId = recipeRepository.getAllIds();
        if (allId.isEmpty()) {
            log.warn("IN getRandomRecipe recipe not found");
            throw new RecipeNotFoundException("Recipe not found");
        }
        Long recipeId = allId.get(random.nextInt(allId.size()));

        Optional<RecipeEntity> recipeRepositoryById = recipeRepository.findById(recipeId);
        if (recipeRepositoryById.isPresent()) {
            return recipeRepositoryById.get();
        }
        log.warn("IN getRandomRecipe recipe with id: {} not found", recipeId);
        throw new RecipeNotFoundException("Recipe with this id: " + recipeId + "not found");
    }

    public RecipeEntity saveRecipe(RecipeEntity recipeEntity) {
        RecipeEntity save = recipeRepository.save(recipeEntity);
        log.info("IN saveRecipe recipeEntity successfully saved");
        return save;
    }

    public List<RecipeEntity> getRecipeByIngredients(Long userId) throws RecipeNotFoundException, UserNotFoundException, UserListNotFoundException {
        List<UserListEntity> userListEntityList = userListRepository.findAllByUser(userService.findById(userId));
        if (userListEntityList.isEmpty()) {
            log.warn("IN getRecipeByIngredients cannot found userList by user id: {}", userId);
            throw new UserListNotFoundException("cannot found userList by user id: " + userId);
        }

        log.info("IN getRecipeByIngredients size of list {} elements: {}",userListEntityList.size(), userListEntityList);
        UserListEntity userListEntity = userListEntityList.get(userListEntityList.size() - 1);
        String recommendList = userListEntity.getRecommendList();
        String banList = userListEntity.getBanList();
        String filter = userListEntity.getFilter();

        List<RecipeEntity> recipeEntityList = banFilter(banList);
        recipeEntityList = recommendFilter(recommendList, recipeEntityList);
        recipeEntityList = sortByFilter(filter, recipeEntityList);

        if (recipeEntityList.isEmpty()) {
            log.warn("IN getRecipeByIngredients cannot found with this userList id: {}", userListEntity.getId());
            throw new RecipeNotFoundException("Cannot found with this userList id:" + userListEntity.getId());
        }

        log.info("IN getRecipeByIngredients list successfully filtered {}",recipeEntityList);

        return recipeEntityList;
    }

    public List<RecipeEntity> getAllRecipeEntity() throws RecipeListIsBlankException {
        List<RecipeEntity> recipeEntities = recipeRepository.getRecipeEntities();

        if (recipeEntities == null) {
            log.warn("Recipe entities is blank");
            throw new RecipeListIsBlankException("Recipe entities is blank");
        }
        log.info("Recipes are successfully found");
        return recipeEntities;
    }

    private List<RecipeEntity> sortByFilter(String filter, List<RecipeEntity> recipeEntityList) {
        HashMap<Long, Integer> mapValues = new HashMap<>();

        switch (filter) {
            case "cook_time":
                for (RecipeEntity recipeEntity : recipeEntityList) {
                    mapValues.put(recipeEntity.getId(), Math.toIntExact(recipeEntity.getCookTime()));
                }
                break;
            case "calories":
                for (RecipeEntity recipeEntity : recipeEntityList) {
                    mapValues.put(recipeEntity.getId(), recipeEntity.getCalories());
                }
                break;
            case "cost":
            default:
                for (RecipeEntity recipeEntity : recipeEntityList) {
                    mapValues.put(recipeEntity.getId(), recipeEntity.getCost());
                }
        }
        Map<Long, Integer> sortedByFilter = mapUtils.sortByValue2(true, mapValues);

        recipeEntityList = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : sortedByFilter.entrySet()) {
            recipeRepository.findById(entry.getKey()).ifPresent(recipeEntityList::add);
        }
        return recipeEntityList;
    }

    private List<RecipeEntity> recommendFilter(String recommendFilter, List<RecipeEntity> recipeEntityList) {
        List<String> recommendIngredients = Arrays.asList(recommendFilter.split(" "));
        int profit = 0;

        Map<Long, String> mapIngredient = new HashMap<>();
        Map<Long, Integer> mapProfit = new HashMap<>();

        for (RecipeEntity recipeEntity : recipeEntityList) {
            mapIngredient.put(recipeEntity.getId(), recipeEntity.getIngredients());
        }

        recipeEntityList = new ArrayList<>();
        for (int i = 0; i < mapIngredient.size(); i++ ) {
            for (Map.Entry<Long, String> entry : mapIngredient.entrySet()) {
                Long key = entry.getKey();
                String[] valueList = entry.getValue().split(" ");

                for (String value : valueList) {
                    if (recommendIngredients.contains(value)) {
                        profit++;
                    }
                }
                mapProfit.put(key, profit);
            }
            Long aLong = mapUtils.maxValueFromMap(mapProfit);
            mapProfit = new HashMap<>();
            Optional<RecipeEntity> recipeRepositoryById = recipeRepository.findById(aLong);
            recipeRepositoryById.ifPresent(recipeEntityList::add);
            mapIngredient.remove(aLong);
        }
        return recipeEntityList;
    }

    private List<RecipeEntity> banFilter(String banList) {
        List<String> banIngredients = Arrays.asList(banList.split(" "));

        List<RecipeEntity> recipeEntityList = recipeRepository.getRecipeEntities();
        Map<Long, String> mapIngredient = new HashMap<>();
        for (RecipeEntity recipeEntity : recipeEntityList) {
            mapIngredient.put(recipeEntity.getId(), recipeEntity.getIngredients());
        }

        List<Long> toRemove = new ArrayList<>();
        recipeEntityList = new ArrayList<>();
        for(Map.Entry<Long, String> entry : mapIngredient.entrySet()) {
            Long key = entry.getKey();
            String[] valueList = entry.getValue().split(" ");


            for (String value : valueList) {
                if (banIngredients.contains(value)) {
                    toRemove.add(key);
                    break;
                }
            }
        }

        for (Long aLong : toRemove)
            mapIngredient.remove(aLong);

        for(Map.Entry<Long, String> entry : mapIngredient.entrySet()) {
            Optional<RecipeEntity> recipeRepositoryById = recipeRepository.findById(entry.getKey());
            recipeRepositoryById.ifPresent(recipeEntityList::add);
        }
        return recipeEntityList;
    }

}
