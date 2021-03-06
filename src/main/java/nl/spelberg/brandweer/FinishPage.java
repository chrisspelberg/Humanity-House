package nl.spelberg.brandweer;

import nl.spelberg.brandweer.model.BrandweerConfig;
import nl.spelberg.brandweer.model.ConfigService;
import nl.spelberg.brandweer.model.Person;
import nl.spelberg.brandweer.model.PersonService;
import nl.spelberg.brandweer.model.PhotoCache;
import nl.spelberg.brandweer.model.PhotoService;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.springframework.util.Assert;

public class FinishPage extends AbstractPage {

    @SpringBean(name = "configService")
    private ConfigService configService;

    @SpringBean(name = "personService")
    private PersonService personService;

    @SpringBean(name = "photoService")
    private PhotoService photoService;

    @SpringBean
    private PhotoCache photoCache;

    public FinishPage(Person person) {
        super("Vul je gegevens in");
        Assert.notNull(person, "person is null");

        BrandweerConfig brandweerConfig = configService.getConfig();

        final LoadableDetachableModel<Person> personModel = new PersonLoadableDetachableModel(person, personService);

        add(new Label("name", new PropertyModel<String>(personModel, "name")));
        add(new Label("email", new PropertyModel<String>(personModel, "email")));

        add(new Image("image", new PersonThumbnailImageResource(new PersonDynamicImageResource(personModel,
                photoService), brandweerConfig.getMaxPhotoSize(), photoCache)));

        add(new AbstractAjaxTimerBehavior(Duration.seconds(brandweerConfig.getTimingFinish())) {
            @Override
            protected void onTimer(AjaxRequestTarget target) {
                setResponsePage(HomePage.class);
                setRedirect(true);
            }
        });
    }
}
