package org.vaadin.crudui.demo.ui.view;

import javax.annotation.PostConstruct;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import org.vaadin.crudui.crud.impl.TreeGridCrud;
import org.vaadin.crudui.demo.entity.Category;
import org.vaadin.crudui.demo.service.CategoryService;
import org.vaadin.crudui.demo.ui.MainLayout;
import org.vaadin.crudui.form.FieldProvider;

/**
 * @author XakepSDK
 */
@Route(value = "simple-tree", layout = MainLayout.class)
public class SimpleTreeCrudView extends VerticalLayout {

	protected final CategoryService categoryService;

	public SimpleTreeCrudView(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@PostConstruct
	private void build() {
		TreeGridCrud<Category> crud = new TreeGridCrud<Category>(Category.class);
		crud.getGrid().removeAllColumns();
		crud.getGrid().addHierarchyColumn(Category::getName).setHeader("Name");

		crud.setChildItemProvider(category -> categoryService.findChildren(category));

		crud.getCrudFormFactory().setVisibleProperties("name", "parent", "description");

		FieldProvider<?, ?> nameProvider = createNameField();
		crud.getCrudFormFactory().setFieldProvider("name", nameProvider);
		FieldProvider<?, ?> parentSelector = createParentSelector();
		crud.getCrudFormFactory().setFieldProvider("parent", parentSelector);
		FieldProvider<?, ?> descriptionProvider = createNameField();
		crud.getCrudFormFactory().setFieldProvider("description", descriptionProvider);

		crud.setFindAllOperation(() -> categoryService.findRoots());

		crud.setAddOperation(category -> categoryService.save(category));
		crud.setUpdateOperation(category -> categoryService.save(category));
		crud.setDeleteOperation(category -> categoryService.delete(category));

		crud.setChildItemProvider(category -> categoryService.findChildren(category));

		crud.setShowNotifications(false);
		addAndExpand(crud);
		setSizeFull();
	}

	private <T> FieldProvider<?, ?> createNameField() {

		FieldProvider<Component, T> f = new FieldProvider<Component, T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public HasValueAndElement buildField(T t, String property) {
				TextField nameField = new TextField("Name");
				nameField.setWidth("300px");
				return nameField;
			}
		};
		return f;
	}

	private <T> FieldProvider<?, ?> createDescriptionField() {
		FieldProvider<Component, T> f = new FieldProvider<Component, T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public HasValueAndElement buildField(T t, String property) {
				TextArea descriptionField = new TextArea("Description");
				descriptionField.setWidth("300px");
				return descriptionField;
			}
		};
		return f;
	}

	private <T> FieldProvider<?, ?> createParentSelector() {
		FieldProvider<Component, T> f = new FieldProvider<Component, T>() {
			@Override
			public HasValueAndElement buildField(T t, String property) {
				ComboBox<Category> selector = new ComboBox<Category>("Parent", categoryService.findAll());
				selector.setWidth("300px");
				return selector;
			}
		};
		return f;
	}

}
