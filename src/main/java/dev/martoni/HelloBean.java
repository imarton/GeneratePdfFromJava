package dev.martoni;

import dev.martoni.model.Asset;
import dev.martoni.model.DocumentData;
import dev.martoni.service.PdfService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean
@RequestScoped
public class HelloBean implements Serializable {

    public String getCurrentTime() {
        return new Date().toString();
    }

    public void generatePdf() throws IOException {
        DocumentData data = createMockData();
        PdfService pdfService = new PdfService();
        byte[] pdfBytes = pdfService.generatePdf(data);

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/pdf");
        externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"bizonylat.pdf\"");
        externalContext.setResponseContentLength(pdfBytes.length);

        OutputStream output = externalContext.getResponseOutputStream();
        output.write(pdfBytes);
        output.flush();
        facesContext.responseComplete();
    }

    private DocumentData createMockData() {
        DocumentData data = new DocumentData();
        data.setId("CH1057101-EAKaiDju2SG001A770294");
        data.setIssueDate(new Date());
        data.setIssuedBy("Gipsz Jakab (123456), Dept_12345678 Szervezeti egység megnevezése");
        
        List<Asset> assets = new ArrayList<>();
        assets.add(new Asset("PC", "G2439953", "HP", "EliteDesk 800 G4", "CZC9143BMJ7", "CI13727883 (11735207)"));
        assets.add(new Asset("HDD", "", "Seagate", "(nem beazonosítható)", "Z1ES5MDAL", "CI03683719 (-)"));
        assets.add(new Asset("SIM (belső)", "I0233352", "Magyar Telekom", "(nem beazonosítható)", "8936304319205F", "CI13832431 (11825934)"));
        assets.add(new Asset("Hangfal", "12290358788", "Genius", "(nem beazonosítható)", "ZF392022435825", "CI01582065 (4044836)"));
        
        data.setAssets(assets);
        data.setPreviousOwner("Gipsz Jakab (123456), Dept_12345678 Szervezeti egység megnevezése");
        data.setPreviousLocation("Borsod-Abaúj-Zemplén, Miskolc, 3501 Nagy u. 10., főépület, 1. emelet, 1202. szoba");
        data.setNewOwner("Gipsz Jakab (123456), Dept_12345678 Szervezeti egység megnevezése");
        data.setNewLocation("Borsod-Abaúj-Zemplén, Miskolc, 33501 Nagy u. 10., főépület, 1. emelet, 1201. szoba");
        data.setSigner("Nyerő Benő (654321)");
        
        return data;
    }
}
