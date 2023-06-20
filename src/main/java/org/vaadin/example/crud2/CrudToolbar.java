package org.vaadin.example.crud2;

import com.vaadin.componentfactory.enhancedcrud.BinderCrudEditor;
import com.vaadin.componentfactory.enhancedcrud.Crud;
import com.vaadin.componentfactory.enhancedcrud.CrudEditor;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.Route;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Route("crud-toolbar")
public class CrudToolbar extends VerticalLayout {
    private final Crud<MyDatabase> crud;
    private ContactDataProvider dataProvider;
    private final String EDIT_COLUMN = "vcf-crud-edit-column";
    TextField name = new TextField("Name");
    TextField phoneNo = new TextField("Phone Number");
    TextField email = new TextField("Email");
    TextField street = new TextField("Street");
    TextField city = new TextField("City");
    TextField country = new TextField("County");
    TextField time = new TextField("Time");
    TextField id = new TextField("id");
    Button button = new Button("New Contact", VaadinIcon.PLUS.create());

    FormLayout form = new FormLayout(id,name, phoneNo, email, street, city, country,time);
    Span footer = new Span();
    boolean editMode = false;
    boolean save = false;

    public CrudToolbar() {
        crud = new Crud<>(MyDatabase.class,createEditor());
        time.setVisible(false);
        id.setVisible(false);
        setupGrid();
        setupDataProvider();
        setupToolbar();
        setSizeFull();

        add(crud);
    }

    private void setupGrid() {
        Grid<MyDatabase> grid = crud.getGrid();
        grid.setColumnOrder( grid.getColumnByKey(EDIT_COLUMN),
                grid.getColumnByKey("name"),
                grid.getColumnByKey("email"),
                grid.getColumnByKey("phone"),
                grid.getColumnByKey("street"),
                grid.getColumnByKey("city"),
                grid.getColumnByKey("country"),
                grid.getColumnByKey("time"),
                grid.getColumnByKey("id")
                );
        // Only show these columns (all columns shown by default):
     List<String> visibleColumns = Arrays.asList(
                "name",
                "email",
                "phone",
                "street",
                "City",
                "country",
                EDIT_COLUMN
        );

        grid.getColumns().forEach(column -> {
            String key = column.getKey();
            if (!visibleColumns.contains(key)) {
             grid.removeColumnByKey(key);
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
        binder.forField(id)
                .bind(MyDatabase::getId, MyDatabase::setId);

        binder.forField(time).bind(MyDatabase::getTime,MyDatabase::setTime);
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

                        if (isExist(phoneNo.getValue())) {
                            return ValidationResult.error("Phone number already exists");
                         }
                    return ValidationResult.ok();
                })
                .bind(MyDatabase::getPhone, MyDatabase::setPhone);
        //Validation for email field
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
    private void confirmationBox(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setConfirmButton("confirm",e->{
            save = true;
        });
    }
    private void setupDataProvider() {
        dataProvider = new ContactDataProvider();
        crud.setDataProvider(dataProvider);
        MyDatabase database = new MyDatabase();
        final String[] bUpdatePh = {phoneNo.getValue()};
        //ConfirmDialog dialog = new ConfirmDialog();
        //AtomicBoolean save = new AtomicBoolean(false);
        //--------------------------------------------Delete - Listener -------------------------------------------------------//
        crud.addDeleteListener(deleteEvent ->{
            if (database.deleteContact(id.getValue()))
            {
                dataProvider.delete(deleteEvent.getItem());
                editMode = false;
            }
        });

        //--------------------------------------------Pre - Save - Listener -------------------------------------------------------//
      /*  crud.addPreSaveListener(e ->
        confirmationBox());
*/
        ConfirmDialog dialog = new ConfirmDialog();
        //--------------------------------------------Save - Listener --------------------------------------------------------//
        crud.addSaveListener(saveEvent -> {
            DateFormat date_format_obj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date_obj = new Date();
            String time1 = date_format_obj.format(date_obj);
           if(editMode){
               //
               //int id = database.getId(bUpdatePh[0]);
               if (database.getTimeBYid(id.getValue()).equals(time.getValue())) {
                   database.editContact(id.getValue(), name.getValue(), phoneNo.getValue(), email.getValue(), street.getValue(), city.getValue(), country.getValue(), time1);
               } else{
                   dialog.open();
                   /*
                   dialog.setText("You have updated the contact. Do you want to update the contact again?");

                 dialog.setConfirmButton("YES", event -> {
                       save = true;
                       database.editContact(id, name.getValue(), phoneNo.getValue(), email.getValue(), street.getValue(), city.getValue(), country.getValue(), time1);

                       // save the changes
                       dialog.close();

                   });
                   dialog.setRejectButton("Discard", event -> {
                       crud.cancelSave();
                       save = false;
                       form.setVisible(true);
                       dialog.close();
                   });
                   dialog.setCancelButton("Cancel", event -> {
                       save = false;
                       dialog.close();
                   });

                    dialog.open();*/
                 if (save) {
                       database.editContact(id.getValue(), name.getValue(), phoneNo.getValue(), email.getValue(), street.getValue(), city.getValue(), country.getValue(), time1);
                   }else {
                       Notification.show("Second EDIT");
                   }
               }
           }else{

                dataProvider.persist(saveEvent.getItem());
                database.addContact(name.getValue(), phoneNo.getValue(), email.getValue(), street.getValue(), city.getValue(), country.getValue(),time1);
            }
            saveEvent.getItem().setTime(time1);
            editMode = false;
        });

        //--------------------------------------------add - Pre - Save - Listener--------------------------------------------//

          crud.addPreSaveListener(e -> {
                //int id = database.getId(bUpdatePh[0]);
                  if (editMode && !database.getTimeBYid(id.getValue()).equals(time.getValue())) {
                        // show a confirmation dialog
                      dialog.setText("You have updated the contact. Do you want to update the contact again?");
                      //dialog.open();
                      dialog.setConfirmButton("YES", event -> {
                          save = true;
                          // save the changes
                          dialog.close();

                      });
                      /*dialog.setRejectButton("Discard", event -> {
                            crud.cancelSave();
                            save = false;
                            //form.setVisible(true);
                            dialog.close();
                        });
                        dialog.setCancelButton("Cancel", event -> {
                            save = false;
                            dialog.close();
                        });*/

                    }

            });


      //----------------------------------------------EDIT - LISTENER-------------------------------------------------------//
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
