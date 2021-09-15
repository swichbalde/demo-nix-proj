package com.example.demo.entity.userlist;

import com.example.demo.exception.user.UserNotFoundException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class RequestUserListEntity {

    private String recommendList;
    private String banList;
    private String filter;

    public RequestUserListEntity() {
    }

    public RequestUserListEntity(String recommendList, String banList, String filter) {
        this.recommendList = recommendList;
        this.banList = banList;
        this.filter = filter;
    }

    public UserListEntity toUserList(RequestUserListEntity requestUserListEntity) throws UserNotFoundException {
        UserListEntity userListEntity = new UserListEntity();
        userListEntity.setRecommendList(requestUserListEntity.getRecommendList());
        userListEntity.setBanList(requestUserListEntity.getBanList());
        userListEntity.setFilter(requestUserListEntity.getFilter());

        return userListEntity;
    }
}
