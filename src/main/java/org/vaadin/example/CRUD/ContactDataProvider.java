/*
package org.vaadin.example.CRUD;

import com.vaadin.flow.component.crud.CrudFilter;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import org.vaadin.example.CRUD.Entity.Contact;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.vaadin.example.CRUD.CrudView.createContactList;

public class ContactDataProvider
        extends AbstractBackEndDataProvider<Contact, CrudFilter> {

    // A real app should hook up something like JPA
    final List<Contact> DATABASE = createContactList();


    private Consumer<Long> sizeChangeListener;

    @Override
    protected Stream<Contact> fetchFromBackEnd(
            Query<Contact, CrudFilter> query) {
        int offset = query.getOffset();
        int limit = query.getLimit();

        Stream<Contact> stream = DATABASE.stream();

        if (query.getFilter().isPresent()) {
            stream = stream.filter((Predicate<? super Contact>) predicate(query.getFilter().get()))
                    .sorted((Comparator<? super Contact>) comparator(query.getFilter().get()));
        }

        return stream.skip(offset).limit(limit);
    }

    @Override
    protected int sizeInBackEnd(Query<Contact, CrudFilter> query) {
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

    private static Predicate<? super Contact> predicate(CrudFilter filter) {
        // For RDBMS just generate a WHERE clause
        return filter.getConstraints().entrySet().stream()
                .map(constraint -> (Predicate<Contact>) contact -> {
                    try {
                        Object value = valueOf(constraint.getKey(), contact);
                        return value != null && value.toString()
                                .toLowerCase().contains(constraint
                                        .getValue().toLowerCase());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }).reduce(Predicate::and).orElse(e -> true);
    }

    private static Comparator<? super Contact> comparator(CrudFilter filter) {
        // For RDBMS just generate an ORDER BY clause
        return filter.getSortOrders().entrySet().stream()
                .map(sortClause -> {
                    try {
                        Comparator<Contact> comparator = Comparator
                                .comparing(contact -> (Comparable) valueOf(
                                        sortClause.getKey(), contact));

                        if (sortClause
                                .getValue() == SortDirection.DESCENDING) {
                            comparator = comparator.reversed();
                        }

                        return comparator;
                    } catch (Exception ex) {
                        return (Comparator<Contact>) (o1, o2) -> 0;
                    }
                }).reduce(Comparator::thenComparing).orElse((o1, o2) -> 0);
    }

    private static Object valueOf(String fieldName, Contact contact) {
        try {
            Field field = Contact.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(contact);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void persist(Contact item) {
        if (item.getId() == null) {
            */
/*item.setId(DATABASE.stream().map(Contact::getId)
                    .max(naturalOrder().reversed()).orElse(0) + 1);*//*

        }

        final Optional<Contact> existingItem = find(item.getId());
        if (existingItem.isPresent()) {
            int position = DATABASE.indexOf(existingItem.get());
            DATABASE.remove(existingItem.get());
            DATABASE.add(position, item);
        } else {
            DATABASE.add(item);
        }
    }

    Optional<Contact> find(Long id) {
        return DATABASE.stream().filter(entity -> entity.getId().equals(id))
                .findFirst();
    }

    public void delete(Contact item) {
        DATABASE.removeIf(entity -> entity.getId().equals(item.getId()));
    }


}
*/
