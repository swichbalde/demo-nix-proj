package com.example.demo.service;

import com.example.demo.entity.RecipeEntity;
import com.example.demo.entity.UserListEntity;
import com.example.demo.exception.RecipeListIsBlankException;
import com.example.demo.exception.RecipeNotFoundException;
import com.example.demo.repository.RecipeRepository;
import com.example.demo.util.MapUtils;
import org.springframework.stereotype.Service;
import com.example.demo.repository.UserListRepository;

import java.util.*;

@Service
public class RecipeService {

    final RecipeRepository recipeRepository;
    final UserListRepository userListRepository;
    final MapUtils mapUtils;

    public RecipeService(RecipeRepository recipeRepository,
                         UserListRepository userListRepository, MapUtils mapUtils) {
        this.recipeRepository = recipeRepository;
        this.userListRepository = userListRepository;
        this.mapUtils = mapUtils;
    }

    public RecipeEntity getRandomRecipe() throws RecipeNotFoundException {
        Random random = new Random();
        List<Long> allId = recipeRepository.getAllIds();
        Long recipeId = allId.get(random.nextInt(allId.size()));

        Optional<RecipeEntity> recipeRepositoryById = recipeRepository.findById(recipeId);
        if (recipeRepositoryById.isPresent()) {
            return recipeRepositoryById.get();
        }
        throw new RecipeNotFoundException("Recipe with this id: " + recipeId + "not found");
    }

    public void saveRecipe(RecipeEntity recipeEntity) {
        recipeRepository.save(recipeEntity);
    }

    public List<RecipeEntity> getRecipeByIngredients(String id) {
        UserListEntity userListEntity = userListRepository.findAllById(Long.valueOf(id));
        String recommendList = userListEntity.getRecommend_list();
        String banList = userListEntity.getBan_list();
        String filter = userListEntity.getFilter();

        List<RecipeEntity> recipeEntityList = banFilter(banList);
        recipeEntityList = recommendFilter(recommendList, recipeEntityList);
        recipeEntityList = sortByFilter(filter, recipeEntityList);

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
            throw new RecipeListIsBlankException("Recipe entities is blank");
        }
        return recipeEntities;
    }
}
