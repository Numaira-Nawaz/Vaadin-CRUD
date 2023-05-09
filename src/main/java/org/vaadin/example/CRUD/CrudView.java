/*
package org.vaadin.example.CRUD;

//import com.example.vaadinform.data.Entity.Contact;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.crud.*;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.DebouncePhase;
import com.vaadin.flow.router.Route;
import org.vaadin.example.CRUD.Entity.Contact;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("serial")
@Route("vaadin-crud")
public class CrudView extends VerticalLayout {

        public CrudView() {
        basicCrud();
        // Using unicode spaces so as card does not show any header
        add(" ");
        setEditorPosition();
        editOnDoubleClick();
        internationalization();
        add("  ");
        noFilteringAndSorting();
        customToolbar();
        customGrid();
        customSearch();
        add(
                new Label("These objects are used in the examples above"));
    }

    private void basicCrud() {
        // begin-source-example
        // source-example-heading: Basic CRUD
        Crud<Contact> crud = new Crud<>(Contact.class, createContactEditor());

        ContactDataProvider dataProvider = new ContactDataProvider();

        crud.setDataProvider(dataProvider);
        crud.addSaveListener(e -> dataProvider.persist(e.getItem()));
        crud.addDeleteListener(e -> dataProvider.delete(e.getItem()));

        crud.getGrid().removeColumnByKey("id");
        crud.addThemeVariants(CrudVariant.NO_BORDER);
        add(crud);
        // end-source-example

        //addCard("Basic CRUD", crud);
    }

    // NOTE: heading is an unicode space
    // begin-source-example
    // source-example-heading:
    private CrudEditor<Contact> createContactEditor() {
        TextField firstName = new TextField("First name");
        TextField lastName = new TextField("Last name");
        FormLayout form = new FormLayout(firstName, lastName);

        Binder<Contact> binder = new Binder<>(Contact.class);
        binder.bind(firstName, Contact::getName, Contact::setName);
        binder.bind(lastName, Contact::getStreet, Contact::setStreet);

        return new BinderCrudEditor<>(binder, form);
    }
    // end-source-example

    private void setEditorPosition() {
        // begin-source-example
        // source-example-heading: Editor Position
        Crud<Contact> crud = new Crud<>(Contact.class, createContactEditor());

        crud.setEditorPosition(CrudEditorPosition.ASIDE);

        ContactDataProvider dataProvider = new ContactDataProvider();

        crud.setDataProvider(dataProvider);
        crud.addSaveListener(e -> dataProvider.persist(e.getItem()));
        crud.addDeleteListener(e -> dataProvider.delete(e.getItem()));

        crud.getGrid().removeColumnByKey("id");
        crud.addThemeVariants(CrudVariant.NO_BORDER);
        add(crud);
        // end-source-example

        //add(crud);
    }

    private void editOnDoubleClick() {
        // begin-source-example
        // source-example-heading: Edit on double-click
        Crud<Contact> crud = new Crud<>(Contact.class, createContactEditor());
        Crud.removeEditColumn(crud.getGrid());

        crud.getGrid().addItemDoubleClickListener(
                e -> crud.edit(e.getItem(), Crud.EditMode.EXISTING_ITEM));

        ContactDataProvider dataProvider = new ContactDataProvider();
        crud.setDataProvider(dataProvider);
        crud.addSaveListener(e -> dataProvider.persist(e.getItem()));
        crud.addDeleteListener(e -> dataProvider.delete(e.getItem()));

        crud.getGrid().removeColumnByKey("id");
        crud.addThemeVariants(CrudVariant.NO_BORDER);
        add(crud);
        // end-source-example

        //addCard("Edit on double-click", crud);
    }

    private void noFilteringAndSorting() {
        // begin-source-example
        // source-example-heading: No filtering and sorting
        CrudGrid<Contact> crudGrid = new CrudGrid<>(Contact.class, false);
        Crud<Contact> crud = new Crud<>(Contact.class, crudGrid,
                createContactEditor());

        ContactDataProvider dataProvider = new ContactDataProvider();

        crud.getGrid().removeColumnByKey("id");
        crud.getGrid().setSortableColumns();
        crud.setDataProvider(dataProvider);
        crud.addSaveListener(e -> dataProvider.persist(e.getItem()));
        crud.addDeleteListener(e -> dataProvider.delete(e.getItem()));
        add(crud);
        // end-source-example

        //addCard("No filtering and sorting", crud);
    }

    private void internationalization() {
        // begin-source-example
        // source-example-heading: Internationalization
        Crud<Contact> crud = new Crud<>(Contact.class, createContactEditor());

        crud.getGrid().removeColumnByKey("id");
        crud.setDataProvider(new ContactDataProvider());

        Button updateI18nButton = new Button("Switch to Yorùbá",
                event -> crud.setI18n(createYorubaI18n()));

        add(crud, updateI18nButton);
        // end-source-example

        //addCard("Internationalization", crud, updateI18nButton);
    }

    // NOTE: heading is two unicode spaces
    // begin-source-example
    // source-example-heading:
    private CrudI18n createYorubaI18n() {
        CrudI18n yorubaI18n = CrudI18n.createDefault();

        yorubaI18n.setNewItem("Eeyan titun");
        yorubaI18n.setEditItem("S'atunko eeyan");
        yorubaI18n.setSaveItem("Fi pamo");
        yorubaI18n.setDeleteItem("Paare");
        yorubaI18n.setCancel("Fa'gi lee");
        yorubaI18n.setEditLabel("S'atunko eeyan");

        yorubaI18n.getConfirm().getCancel().setTitle("Akosile");
        yorubaI18n.getConfirm().getCancel()
                .setContent("Akosile ti a o tii fi pamo nbe");
        yorubaI18n.getConfirm().getCancel().getButton()
                .setDismiss("Se atunko sii");
        yorubaI18n.getConfirm().getCancel().getButton().setConfirm("Fa'gi lee");

        yorubaI18n.getConfirm().getDelete().setTitle("Amudaju ipare");
        yorubaI18n.getConfirm().getDelete().setContent(
                "Se o da o l'oju pe o fe pa eeyan yi re? Igbese yi o l'ayipada o.");
        yorubaI18n.getConfirm().getDelete().getButton()
                .setDismiss("Da'wo duro");
        yorubaI18n.getConfirm().getDelete().getButton().setConfirm("Paare");

        return yorubaI18n;
    }
    // end-source-example

    private void customToolbar() {
        // begin-source-example
        // source-example-heading: Custom toolbar
        Crud<Contact> crud = new Crud<>(Contact.class, createContactEditor());

        Span footer = new Span();
        footer.getElement().getStyle().set("flex", "1");

        Button newItemButton = new Button("Add Contact ...");
        newItemButton.addClickListener(
                e -> crud.edit(new Contact(), Crud.EditMode.NEW_ITEM));

        crud.setToolbar(footer, newItemButton);

        ContactDataProvider dataProvider = new ContactDataProvider();
        dataProvider.setSizeChangeListener(
                count -> footer.setText("Total: " + count));

        crud.getGrid().removeColumnByKey("id");
        crud.setDataProvider(dataProvider);
        crud.addSaveListener(e -> dataProvider.persist(e.getItem()));
        crud.addDeleteListener(e -> dataProvider.delete(e.getItem()));
        add(crud);
        // end-source-example

        //addCard("Custom toolbar", crud);
    }

    private void customGrid() {
        // begin-source-example
        // source-example-heading: Custom Grid
        Grid<Contact> grid = new Grid<>();
        Crud<Contact> crud = new Crud<>(Contact.class, grid,
                createContactEditor());

        ContactDataProvider dataProvider = new ContactDataProvider();
        crud.setDataProvider(dataProvider);
        crud.addSaveListener(e -> dataProvider.persist(e.getItem()));
        crud.addDeleteListener(e -> dataProvider.delete(e.getItem()));

        Crud.addEditColumn(grid);
        grid.addColumn(TemplateRenderer.<Contact> of(
                "<img src=[[item.photoSource]] style=\"height: 40px; border-radius: 50%;\">")
                .withProperty("photoSource", CrudView::randomProfilePictureUrl))
                .setWidth("60px").setFlexGrow(0);
        grid.addColumn(Contact::getName).setHeader("Name");
        grid.addColumn(Contact::getStreet).setHeader("Street");
        add(crud);
        // end-source-example

       // addCard("Custom Grid", crud);
    }

    private void customSearch() {
        // begin-source-example
        // source-example-heading: Custom search
        Grid<Contact> grid = new Grid<>(Contact.class);
        Crud<Contact> crud = new Crud<>(Contact.class, grid,
                createContactEditor());

        List<Contact> database = createContactList();

        Function<String, Stream<Contact>> filter = query -> {
            Stream<Contact> result = database.stream();

            if (!query.isEmpty()) {
                final String f = query.toLowerCase();
                result = result.filter(p -> (p.getName() != null)
                        && (p.getName().toLowerCase().contains(f))
                        || p.getStreet().toLowerCase().contains(f));
            }

            return result;
        };

        DataProvider<Contact, String> dataProvider = new CallbackDataProvider<>(
                query -> filter.apply(query.getFilter().orElse("")),
                query -> (int) filter.apply(query.getFilter().orElse(""))
                        .count());

        ConfigurableFilterDataProvider<Contact, Void, String> filterableDataProvider = dataProvider
                .withConfigurableFilter();

        grid.setDataProvider(filterableDataProvider);
        grid.removeColumnByKey("id");

        TextField searchBar = new TextField();
        searchBar.setPlaceholder("Search...");
        searchBar.setWidth("100%");
        searchBar.setValueChangeMode(ValueChangeMode.EAGER);
        searchBar.setPrefixComponent(VaadinIcon.SEARCH.create());

        Icon closeIcon = new Icon("lumo", "cross");
        closeIcon.setVisible(false);
        ComponentUtil.addListener(closeIcon, ClickEvent.class,
                (ComponentEventListener) e -> searchBar.clear());
        searchBar.setSuffixComponent(closeIcon);

        searchBar.getElement().addEventListener("value-changed", event -> {
            closeIcon.setVisible(!searchBar.getValue().isEmpty());
            filterableDataProvider.setFilter(searchBar.getValue());
        }).debounce(300, DebouncePhase.TRAILING);

        crud.setToolbar(searchBar);
        crud.getElement().getStyle().set("flex-direction", "column-reverse");
        add(crud);
        // end-source-example

       // addCard("Custom search", crud);
    }

    // Dummy database
    private static final String[] FIRSTS = { "James", "Mary", "John",
            "Patricia", "Robert", "Jennifer" };
    private static final String[] LASTS = { "Smith", "Johnson", "Williams",
            "Brown" };

    static List<Contact> createContactList() {
        return (List<Contact>) IntStream
                .rangeClosed(1, 50).mapToObj(i -> new Contact());
    }

    private static String randomProfilePictureUrl(Object context) {
        return "https://randomuser.me/api/portraits/thumb/"
                + (Math.random() > 0.5 ? "men" : "women") + '/'
                + (1 + (int) (Math.random() * 100)) + ".jpg";
    }

    // begin-source-example
    // source-example-heading: Example Classes
    // Person Bean
    */
/*public static class Contact implements Cloneable {
        private Integer id;
        private String firstName;
        private String lastName;

        *//*
*/
/**
         * No-arg constructor required by Crud to be able to instantiate a new
         * bean when the new item button is clicked.
         *//*
*/
/*
        public Contact() {
        }

        public Contact(Integer id, String firstName, String lastName) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Override
        public Contact clone() {
            try {
                return (Contact) super.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }
*//*

    // Person data provider

    // end-source-example
}
*/
