package com.lahzouz.graphqlfilter.filter;

import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLInputType;

import java.util.function.Function;
import java.util.function.Predicate;

import static graphql.Scalars.GraphQLBoolean;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;

/**
 * A filter that can be used inside other nested filters, such as the {@link MainFilter}
 */
public interface FilterField<T, Q> extends Filter<T, Q> {
    /**
     * A filter that tests if a value is null.
     */
    static <T> FilterField<T, Boolean> isNull() {
        return create("isNull", "Tests if the value is null", GraphQLBoolean, query -> value -> query == (value == null));
    }

    /**
     * A helper method to easily create a FilterField.
     *
     * @param name            name of the filter
     * @param description     description of the filter
     * @param type            GraphQl type of the filter
     * @param createPredicate a function that creates a predicate based on the filter the user defined
     * @param <T>             The predicate input type
     * @param <Q>             The Java type mapped from the GraphQL input type
     */
    static <T, Q> FilterField<T, Q> create(String name, String description, GraphQLInputType type, Function<Q, Predicate<T>> createPredicate) {
        return new FilterField<T, Q>() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public Predicate<T> createPredicate(Q query) {
                return createPredicate.apply(query);
            }

            @Override
            public GraphQLInputType getType() {
                return type;
            }
        };
    }

    /**
     * Creates the field which is used to construct the GraphQL input type.
     */
    default GraphQLInputObjectField toObjectField() {
        return newInputObjectField()
                .name(getName())
                .description(getDescription())
                .type(getType())
                .build();
    }

    /**
     * The name of the field in the GraphQLInputObject
     */
    String getName();

    /**
     * The description of the field in the GraphQLInputObject
     */
    String getDescription();
}
