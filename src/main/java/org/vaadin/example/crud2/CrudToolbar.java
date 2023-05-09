package org.vaadin.example.crud2;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.vaadin.example.crud2.MyDatabase.addContact;
import static org.vaadin.example.crud2.MyDatabase.editContact;

@Route("crud-toolbar")
public class CrudToolbar extends VerticalLayout {

    private final Crud<MyDatabase> crud;
    private ContactDataProvider dataProvider;
    private final String EDIT_COLUMN = "vaadin-crud-edit-column";
    TextField name = new TextField("Name");
    TextField phoneNo = new TextField("Phone Number");
    TextField email = new TextField("Email");
    TextField street = new TextField("Street");
    TextField city = new TextField("City");
    TextField country = new TextField("County");
    Button button = new Button("New Contact", VaadinIcon.PLUS.create());

    FormLayout form = new FormLayout(name, phoneNo, email, street, city, country);
    Span footer = new Span();
    boolean editMode = false;

    public CrudToolbar() {
        crud = new Crud<>(
                MyDatabase.class,
                createEditor()
        );
        setupGrid();
        setupDataProvider();
        setupToolbar();
        setSizeFull();

        add(crud);
    }

    private void setupGrid() {
        Grid<MyDatabase> grid = crud.getGrid();
        grid.setSizeFull();
       /* List<GridSortOrder<MyDatabase>> sortOrder = Collections.singletonList(new GridSortOrder<>(grid.getColumnByKey("name"),
                SortDirection.ASCENDING));
        grid.sort(sortOrder);*/
        //editMode = true;
        grid.addItemDoubleClickListener(event -> crud.edit(event.getItem(),
                Crud.EditMode.EXISTING_ITEM));

        grid.setColumnOrder(
                grid.getColumnByKey(EDIT_COLUMN),
                //grid.getColumnByKey("id"),
                grid.getColumnByKey("name"),
                grid.getColumnByKey("email"),
                grid.getColumnByKey("phone"),
                grid.getColumnByKey("street"),
                grid.getColumnByKey("city"),
                grid.getColumnByKey("country")

        );

        // Only show these columns (all columns shown by default):
      List<String> visibleColumns = Arrays.asList(
                "name",
                "email",
                "phone",
                EDIT_COLUMN
        );
        grid.getColumns().forEach(column -> {
            String key = column.getKey();
            if (!visibleColumns.contains(key)) {
                grid.removeColumn(column);
            }
        });
    }

    private void setupToolbar() {

            // tag::snippet[]
            //Html total = new Html("<span>Total: <b>" + dataProvider.DATABASE.size() + "</b> Contacts</span>");

            button.addClickListener(event -> {
                crud.edit(new MyDatabase(), Crud.EditMode.NEW_ITEM);
            });
            button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

            dataProvider.setSizeChangeListener(
                count -> footer.setText("Total:" + count));
            footer.getElement().getStyle().set("flex", "1");
            HorizontalLayout toolbar = new HorizontalLayout(button);

            toolbar.setAlignItems(FlexComponent.Alignment.CENTER);
            toolbar.setFlexGrow(1, toolbar);
            toolbar.setWidthFull();
            toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
            toolbar.setSpacing(true);
            crud.setToolbar(footer, toolbar);
    }

    public boolean isExist(String ph){
        boolean isExist = false;
        List<MyDatabase> items = MyDatabase.getItems();
        for (MyDatabase item : items) {
            if (item.getPhone().equals(ph)) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }
    private CrudEditor<MyDatabase> createEditor() {
        Binder<MyDatabase> binder = new Binder<>(MyDatabase.class);
        binder.forField(name).asRequired().bind(MyDatabase::getName, MyDatabase::setName);
        //binder.forField(phoneNo).asRequired().bind(MyDatabase::getPhone,MyDatabase::setPhone);
        binder.forField(phoneNo)
                .asRequired()
                .withValidator(new StringLengthValidator(
                        "Phone number must be 7-9 digits long", 7, 9))
                .withValidator((phoneNumber, context) -> {
                    // Validate that the phone number is not the same as any existing phone number
                    List<MyDatabase> items = dataProvider.DATABASE;
                    for(int i = 0;i<items.size();  i++){
                        if (items.get(i).getPhone() == phoneNumber){
                           return ValidationResult.ok();
                        }

                    }
                    for (MyDatabase item : items) {
                        if (item.getPhone().equals(phoneNo.getValue())) {
                            return ValidationResult.error("Phone number already exists");
                        }
                    }

                    return ValidationResult.ok();
                })
                .bind(MyDatabase::getPhone, MyDatabase::setPhone);


        // Validation for email field
            binder.forField(email)
                    .asRequired()
                    .withValidator(new EmailValidator("Invalid email address"))
                    .bind(MyDatabase::getEmail, MyDatabase::setEmail);
            binder.forField(name).bind(MyDatabase::getName, MyDatabase::setName);
            binder.forField(street).bind(MyDatabase::getStreet, MyDatabase::setStreet);
            binder.forField(city).bind(MyDatabase::getCity, MyDatabase::setCity);
            binder.forField(country).bind(MyDatabase::getCountry, MyDatabase::setCountry);
            return new BinderCrudEditor<>(binder, form);
    }

    private void setupDataProvider() {
        dataProvider = new ContactDataProvider();
        crud.setDataProvider(dataProvider);
        MyDatabase database = new MyDatabase();

        crud.addDeleteListener(deleteEvent ->{
            if (database.deleteContact(phoneNo.getValue())){
                dataProvider.delete(deleteEvent.getItem());
            }
        });
        final String[] bUpdatePh = {phoneNo.getValue()};
        crud.addSaveListener(saveEvent -> {
            if (editMode) {
                int id = database.getId(bUpdatePh[0]);
                editContact(id,name.getValue(), phoneNo.getValue(), email.getValue(),street.getValue() ,city.getValue() ,country.getValue());
            }else {

                dataProvider.persist(saveEvent.getItem());
                addContact(name.getValue(), phoneNo.getValue(), email.getValue(), street.getValue(), city.getValue(), country.getValue());

                dataProvider.refreshAll();
            }
            editMode = false;
        });

      crud.addEditListener(editEvent-> {
          editMode=true;
          bUpdatePh[0] = phoneNo.getValue();
            phoneNo.setPreventInvalidInput(true); // disable client-side validation
            phoneNo.addValueChangeListener(event -> {

           if (isExist(phoneNo.getValue())) {
                    phoneNo.setInvalid(true);
                    phoneNo.setErrorMessage("Phone number already exists");
                } else {
                    phoneNo.setInvalid(false);
                }

            });
        });
    }
}
