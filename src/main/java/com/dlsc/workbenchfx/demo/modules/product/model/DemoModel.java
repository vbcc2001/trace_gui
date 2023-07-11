package com.dlsc.workbenchfx.demo.modules.product.model;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.Section;
import com.dlsc.formsfx.model.util.ResourceBundleService;
import com.dlsc.formsfx.model.validators.DoubleRangeValidator;
import com.dlsc.formsfx.model.validators.IntegerRangeValidator;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.controls.SimpleCheckBoxControl;
import com.dlsc.formsfx.view.controls.SimpleRadioButtonControl;
import com.dlsc.formsfx.view.util.ColSpan;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class is used to create the form and holds all the necessary data. This
 * class acts as a singleton where the current instance is available using
 * {@code getInstance}.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public final class DemoModel {

    private Product product = new Product();

    /**
     * These are the resource bundles for german and english.
     */
    private ResourceBundle rbDE = ResourceBundle.getBundle("demo-locale", new Locale("de", "CH"));
    private ResourceBundle rbEN = ResourceBundle.getBundle("demo-locale", new Locale("en", "UK"));

    /**
     * The default locale is English, thus the {@code ResourceBundleService} is
     * initialised with it.
     */
    private ResourceBundleService rbs = new ResourceBundleService(rbEN);

    private Form formInstance;

    /**
     * Creates or simply returns to form singleton instance.
     *
     * @return Returns the form instance.
     */
    public Form getFormInstance() {
        if (formInstance == null) {
            createForm();
        }

        return formInstance;
    }

    /**
     * Creates a new form instance with the required information.
     */
    private void createForm() {
        formInstance = Form.of(
                Group.of(
                        Field.ofStringType(product.nameProperty())
                                .label("产品名称")
                                .placeholder("product_placeholder")
                                .required("required_error_message")
                                .validate(StringLengthValidator.atLeast(2, "product_error_message")),
                        Field.ofStringType(product.isoProperty())
                                .label("ISO_3166_label")
                                .placeholder("ISO_3166_placeholder")
                                .required("required_error_message")
                                .validate(StringLengthValidator.exactly(2, "ISO_3166_error_message")),
                        Field.ofBooleanType(product.independenceProperty())
                                .label("independent_label")
                                .required("required_error_message"),
                        Field.ofDate(product.getIndependenceDay())
                                .label("independent_since_label")
                                .required("required_error_message")
                                .placeholder("independent_since_placeholder")
                ),
                Section.of(
                        Field.ofStringType(product.currencyShortProperty())
                                .label("hello")
                                .placeholder("currency_placeholder")
                                .validate(StringLengthValidator.exactly(3, "currency_error_message")),
                                //.span(ColSpan.HALF),
                        Field.ofStringType(product.currencyLongProperty())
                                .label("currency_long_label")
                                .placeholder("currency_long_placeholder")
                                .span(ColSpan.HALF),
                        Field.ofDoubleType(product.areaProperty())
                                .label("area_label")
                                .format("format_error_message")
                                .placeholder("area_placeholder")
                                .validate(DoubleRangeValidator.atLeast(1, "area_error_message"))
                                .span(ColSpan.HALF),
                        Field.ofStringType(product.tldProperty())
                                .label("internet_TLD_label")
                                .placeholder("internet_TLD_placeholder")
                                .span(ColSpan.HALF)
                                .validate(
                                        StringLengthValidator.exactly(3, "internet_TLD_error_message"),
                                        RegexValidator.forPattern("^.[a-z]{2}$", "internet_TLD_format_error_message")
                                ),
                        Field.ofStringType(product.dateFormatProperty())
                                .label("date_format_label")
                                .placeholder("date_format_placeholder")
                                .multiline(true)
                                .span(ColSpan.HALF)
                                .validate(StringLengthValidator.atLeast(8, "date_format_error_message")),
                        Field.ofSingleSelectionType(product.allSidesProperty(), product.driverSideProperty())
                                .required("required_error_message")
                                .label("driving_label")
                                .span(ColSpan.HALF)
                                .render(new SimpleRadioButtonControl<>()),
                        Field.ofStringType(product.timeZoneProperty())
                                .label("time_zone_label")
                                .placeholder("time_zone_placeholder")
                                .span(ColSpan.HALF)
                                .validate(StringLengthValidator.exactly(3, "time_zone_error_message")),
                        Field.ofStringType(product.summerTimeZoneProperty())
                                .label("summer_time_zone_label")
                                .placeholder("summer_time_zone_placeholder")
                                .span(ColSpan.HALF)
                                .validate(StringLengthValidator.atLeast(3, "summer_time_zone_error_message"))
                ).title("other_information_label"),
                Section.of(
                        Field.ofSingleSelectionType(product.allCapitalsProperty(), product.capitalProperty())
                                .label("capital_label")
                                .required("required_error_message")
                                .tooltip("capital_tooltip")
                                .span(ColSpan.HALF),
                        Field.ofIntegerType(product.populationProperty())
                                .label("population_label")
                                .format("format_error_message")
                                .placeholder("population_placeholder")
                                .required("required_error_message")
                                .span(ColSpan.HALF)
                                .validate(IntegerRangeValidator.atLeast(1, "population_error_message")),
                        Field.ofMultiSelectionType(product.allContinentsProperty(), product.continentsProperty())
                                .label("continent_label")
                                .required("required_error_message")
                                .span(ColSpan.HALF)
                                .render(new SimpleCheckBoxControl<>()),
                        Field.ofMultiSelectionType(product.allCitiesProperty(), product.germanCitiesProperty())
                                .label("german_cities_label")
                                .span(ColSpan.HALF),
                        Field.ofPasswordType("secret")
                                .label("secret_label")
                                .required("required_error_message")
                                .span(ColSpan.HALF)
                                .validate(StringLengthValidator.between(1, 10, "secret_error_message"))
                ).title("cities_and_population_label")
        ).title("form_label")
                .i18n(rbs);

    }

    /**
     * Sets the locale of the form.
     *
     * @param language The language identifier for the new locale. Either DE or EN.
     */
    public void translate(String language) {
        switch (language) {
            case "EN":
                rbs.changeLocale(rbEN);
                break;
            case "DE":
                rbs.changeLocale(rbDE);
                break;
            default:
                throw new IllegalArgumentException("Not a valid locale");
        }
    }

    public Product getCountry() {
        return product;
    }

}
