package org.vaadin.example;

import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.HtmlObject;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;

@Route("/embedded-pdf-download-handler")
public class PdfWithDownloadHandlerView extends VerticalLayout implements HasUrlParameter<String> {
    private final HtmlObject pdfViewer;
    private StreamRegistration streamRegistration;

    public PdfWithDownloadHandlerView() {
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

        DownloadHandler handler = DownloadHandler.fromInputStream(evt ->
                new DownloadResponse(
                        PdfView.class.getResourceAsStream("/example.pdf"),
                        "example.pdf",
                        "application/pdf",
                        -1));
        this.pdfViewer.setData(handler);

        // Below is what I tried to get the download handler to work
//        this.streamRegistration = VaadinSession.getCurrent().getResourceRegistry().registerResource(handler);
//        final URI resourceUri = this.streamRegistration.getResourceUri();
//
//        final String namedDest = Optional.ofNullable(param).filter(Predicate.not(String::isBlank)).orElse("third");
//        this.pdfViewer.setData(resourceUri.getPath() + "#nameddest=" + namedDest);
    }
}
