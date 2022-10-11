package agstack.gramophone.ui.createnewpost.view;

public enum ItemType {LOAD(10), ITEM(11),ADVERTISE_PRODUCT(12),PROFILE_SUGGESTION(13),OTHER(14);
    private final int typeCode;

    ItemType(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return this.typeCode;
    }
}