package org.vaadin.example;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.HtmlObject;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Predicate;

@Route("/embedded-pdf")
public class PdfView extends VerticalLayout implements HasUrlParameter<String> {
    private final HtmlObject pdfViewer;
    private StreamRegistration streamRegistration;

    public PdfView() {
        this.pdfViewer = new HtmlObject();
        this.pdfViewer.setType("application/pdf");
        this.pdfViewer.setSizeFull();
        add(this.pdfViewer);
        setSizeFull();
    }


    @Override
    protected void onDetach(DetachEvent detachEvent) {
        if (this.streamRegistration != null) {
            this.streamRegistration.unregister();
            this.streamRegistration = null;
        }
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String param) {
        if (this.streamRegistration != null) {
            this.streamRegistration.unregister();
            this.streamRegistration = null;
        }

        final StreamResource resource = new StreamResource("example.pdf", () -> PdfView.class.getResourceAsStream("/example.pdf"));
        this.streamRegistration = VaadinSession.getCurrent().getResourceRegistry().registerResource(resource);
        final URI resourceUri = this.streamRegistration.getResourceUri();

        final String namedDest = Optional.ofNullable(param).filter(Predicate.not(String::isBlank)).orElse("third");
        this.pdfViewer.setData(resourceUri.getPath() + "#nameddest=" + namedDest);
    }
}
