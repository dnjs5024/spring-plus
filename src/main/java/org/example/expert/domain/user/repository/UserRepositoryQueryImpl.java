package org.example.expert.domain.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;


import static org.example.expert.domain.user.entity.QUser.user;

import org.example.expert.domain.user.dto.response.UserSearchResponse;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryQueryImpl implements UserRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<UserSearchResponse> searchUsers(String name) {
        return jpaQueryFactory
            .select(Projections.constructor(UserSearchResponse.class,
                user.id,
                user.email,
                user.userName
            ))
            .from(user)
            .where(user.userName.eq(name))
            .fetch();
    }
}
