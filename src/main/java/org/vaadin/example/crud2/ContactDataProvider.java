package org.vaadin.example.crud2;



import com.vaadin.componentfactory.enhancedcrud.CrudFilter;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Comparator.naturalOrder;

public class ContactDataProvider extends AbstractBackEndDataProvider<MyDatabase, CrudFilter> {

    List<MyDatabase> DATABASE = new ArrayList<>(MyDatabase.getItems());

    private Consumer<Long> sizeChangeListener;

    @Override
    protected Stream<MyDatabase> fetchFromBackEnd(Query<MyDatabase, CrudFilter> query) {
        int offset = query.getOffset();
        int limit = query.getLimit();

        Stream<MyDatabase> stream = DATABASE.stream();

        if (query.getFilter().isPresent()) {
            stream = stream
                    .filter(predicate(query.getFilter().get()))
                    .sorted(comparator(query.getFilter().get()));
        }

        return stream.skip(offset).limit(limit);
    }

    @Override
    protected int sizeInBackEnd(Query<MyDatabase, CrudFilter> query) {
        // For RDBMS just execute a SELECT COUNT(*) ... WHERE query
        long count = fetchFromBackEnd(query).count();

        if (sizeChangeListener != null) {
            sizeChangeListener.accept(count);
        }

        return (int) count;
    }

    void setSizeChangeListener(Consumer<Long> listener) {
        sizeChangeListener = listener;
    }

    private static Predicate<MyDatabase> predicate(CrudFilter filter) {
        // For RDBMS just generate a WHERE clause
        return filter.getConstraints().entrySet().stream()
                .map(constraint -> (Predicate<MyDatabase>) myDatabase -> {
                    try {
                        Object value = valueOf(constraint.getKey(), myDatabase);
                        return value != null && value.toString().toLowerCase()
                                .contains(constraint.getValue().toLowerCase());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .reduce(Predicate::and)
                .orElse(e -> true);
    }

    private static Comparator<MyDatabase> comparator(CrudFilter filter) {
        // For RDBMS just generate an ORDER BY clause
        return filter.getSortOrders().entrySet().stream()
                .map(sortClause -> {
                    try {
                        Comparator<MyDatabase> comparator = Comparator.comparing(contact ->
                                (Comparable) valueOf(sortClause.getKey(), contact)
                        );

                        if (sortClause.getValue() == SortDirection.ASCENDING) {
                            comparator = comparator.reversed();
                        }

                        return comparator;

                    } catch (Exception ex) {
                        return (Comparator<MyDatabase>) (o1, o2) -> 0;
                    }
                })
                .reduce(Comparator::thenComparing)
                .orElse((o1, o2) -> 0);
    }

    private static Object valueOf(String fieldName, MyDatabase contact) {
        try {
            Field field = MyDatabase.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(contact);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    void persist(MyDatabase item) {
        if (item.getPhone() == null) {
            item.setPhone(DATABASE
                    .stream()
                    .map(MyDatabase::getPhone)
                    .min(naturalOrder())
                    .orElse(String.valueOf(0)) + 1);
        }

        final Optional<MyDatabase> existingItem = find(item.getPhone());
        if (existingItem.isPresent()) {
                int position = DATABASE.indexOf(existingItem.get());
                DATABASE.remove(existingItem.get());
                DATABASE.add(position, item);
            } else {
                DATABASE.add(item);
            }

    }

    Optional<MyDatabase> find(String phone) {
        return DATABASE
                .stream()
                .filter(entity -> entity.getPhone().equals(phone))
                .findFirst();
    }
    public int getCount(){
        return DATABASE.size();
    }

    void delete(MyDatabase item) {
        DATABASE.removeIf(entity -> entity.getId().equals(item.getId()));
    }



}