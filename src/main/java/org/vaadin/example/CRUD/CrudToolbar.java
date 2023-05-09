/*
package org.vaadin.example.CRUD;


import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.vaadin.example.CRUD.Entity.Contact;

import java.util.Arrays;
import java.util.List;

@Route("crud-toolbar")
public class CrudToolbar extends Div {
  private Crud<Contact> crud;
  private ContactDataProvider dataProvider;

  private String name = "Nmae";
  private String phoneNumber = "phone Number";
  private String email = "Email";
  private String street = "Street";
  private String city = "City";
  private String country = "Country";
  private String EDIT_COLUMN = "vaadin-crud-edit-column";

  public CrudToolbar() {
    crud = new Crud<>(
            Contact.class,
            createEditor()
    );

    //setupGrid();
    setupDataProvider();
    setupToolbar();

    add(crud);
  }

  private CrudEditor<Contact> createEditor() {
    TextField name = new TextField("Name");
    TextField phone_number = new TextField("Phone Number");
    TextField street = new TextField("Street");
    TextField city = new TextField("City");
    TextField email = new TextField("Email");
    TextField country = new TextField("Country");
    FormLayout form = new FormLayout(name,email,phone_number,street,country);

    Binder<Contact> binder = new Binder<>(Contact.class);
    binder.forField(name).asRequired().bind(Contact::getName, Contact::setName);
    binder.forField(phone_number).asRequired().bind(Contact::getPhoneNumber, Contact::setPhoneNumber);
    binder.forField(street).asRequired().bind(Contact::getStreet,Contact::setStreet);
    binder.forField(city).asRequired().bind(Contact::getCity, Contact::setCity);
    binder.forField(email).asRequired().bind(Contact::getEmail, Contact::setEmail);
    binder.forField(country).asRequired().bind(Contact::getCountry,Contact::setCountry);

    return new BinderCrudEditor<>(binder, form);
  }

  private void setupGrid() {
    Grid<Contact> grid = crud.getGrid();

    // Only show these columns (all columns shown by default):
    List<String> visibleColumns = Arrays.asList(
            name,
            email,
            phoneNumber,
            street,
            country,
            city,
            EDIT_COLUMN
    );
    grid.getColumns().forEach(column -> {
      String key = column.getKey();
      if (!visibleColumns.contains(key)) {
        grid.removeColumn(column);
      }
    });

    // Reorder the columns (alphabetical by default)
 */
/*   grid.setColumnOrder(
            grid.getColumnByKey(name),
            grid.getColumnByKey(phoneNumber),
            grid.getColumnByKey(email),
            grid.getColumnByKey(street),
            grid.getColumnByKey(city),
            grid.getColumnByKey(country),
            grid.getColumnByKey(EDIT_COLUMN)
    );*//*

  }

  private void setupDataProvider() {
    dataProvider = new ContactDataProvider();
    //crud.setDataProvider(dataProvider);
    crud.addDeleteListener(deleteEvent ->
            dataProvider.delete(deleteEvent.getItem())
    );
    */
/*crud.addSaveListener(saveEvent ->
            (saveEvent.getItem())
    );*//*

  }

  private void setupToolbar() {
    // tag::snippet[]
    Html total = new Html("<span>Total: <b>" + dataProvider.DATABASE.size() + "</b> employees</span>");

    Button button = new Button("New employee", VaadinIcon.PLUS.create());
    button.addClickListener(event -> {
      crud.edit(new Contact(), Crud.EditMode.NEW_ITEM);
    });
    button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    HorizontalLayout toolbar = new HorizontalLayout(total, button);
    toolbar.setAlignItems(FlexComponent.Alignment.CENTER);
    toolbar.setFlexGrow(1, toolbar);
    toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
    toolbar.setSpacing(false);

    crud.setToolbar(toolbar);
    // end::snippet[]
  }
}
*/
