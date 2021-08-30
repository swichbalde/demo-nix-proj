package com.example.demo.service.impl;

import com.example.demo.entity.RecipeEntity;
import com.example.demo.entity.UserListEntity;
import com.example.demo.exception.list.RecipeListIsBlankException;
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
        Long recipeId = allId.get(random.nextInt(allId.size()));

        Optional<RecipeEntity> recipeRepositoryById = recipeRepository.findById(recipeId);
        if (recipeRepositoryById.isPresent()) {
            return recipeRepositoryById.get();
        }
        log.warn("IN getRandomRecipe recipe with id: {} not found", recipeId);
        throw new RecipeNotFoundException("Recipe with this id: " + recipeId + "not found");
    }

    public void saveRecipe(RecipeEntity recipeEntity) {
        recipeRepository.save(recipeEntity);
        log.info("IN saveRecipe recipeEntity successfully saved");
    }

    public List<RecipeEntity> getRecipeByIngredients(String id) throws UserNotFoundException {
        List<UserListEntity> userListEntityList = userListRepository.findAllByUser(userService.findById(Long.valueOf(id)));
        log.info("IN saveRecipe size of list {}",userListEntityList.size());
        UserListEntity userListEntity = userListEntityList.get(userListEntityList.size() - 1);
        String recommendList = userListEntity.getRecommendList();
        String banList = userListEntity.getBanList();
        String filter = userListEntity.getFilter();

        List<RecipeEntity> recipeEntityList = banFilter(banList);
        recipeEntityList = recommendFilter(recommendList, recipeEntityList);
        recipeEntityList = sortByFilter(filter, recipeEntityList);

        log.info("IN saveRecipe list successfully filtered {}",recipeEntityList);

        return recipeEntityList;
    }

    private List<RecipeEntity> sortByFilter(String filter, List<RecipeEntity> recipeEntityList) {
        HashMap<Long, Integer> mapValues = new HashMap<>();

        switch (filter) {
            case "cook_time":
                for (RecipeEntity recipeEntity : recipeEntityList) {
                    mapValues.put(recipeEntity.getId(), Math.toIntExact(recipeEntity.getCook_time()));
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
        HashMap<Long, Integer> sortedByFilter = mapUtils.sortByValue(mapValues);

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

    public List<RecipeEntity> getAllRecipeEntity() throws RecipeListIsBlankException {
        List<RecipeEntity> recipeEntities = recipeRepository.getRecipeEntities();

        if (recipeEntities == null) {
            log.warn("Recipe entities is blank");
            throw new RecipeListIsBlankException("Recipe entities is blank");
        }
        log.info("Recipes are successfully found");
        return recipeEntities;
    }
}
