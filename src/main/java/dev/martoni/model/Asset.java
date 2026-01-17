package dev.martoni.model;

public class Asset {
    private String category;
    private String inventoryNumber;
    private String brand;
    private String model;
    private String serialNumber;
    private String smId;

    public Asset(String category, String inventoryNumber, String brand, String model, String serialNumber, String smId) {
        this.category = category;
        this.inventoryNumber = inventoryNumber;
        this.brand = brand;
        this.model = model;
        this.serialNumber = serialNumber;
        this.smId = smId;
    }

    public String getCategory() { return category; }
    public String getInventoryNumber() { return inventoryNumber; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public String getSerialNumber() { return serialNumber; }
    public String getSmId() { return smId; }
}
