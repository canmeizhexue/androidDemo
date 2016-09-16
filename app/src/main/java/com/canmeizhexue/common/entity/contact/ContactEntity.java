package com.canmeizhexue.common.entity.contact;

/**联系人
 * Created by silence on 2016/9/16.
 */
public class ContactEntity {
    private String name;
    private String phone;
    private String sortKey;
    private int contactId;
    private String lookupKey;
    private String firstChar;

    public ContactEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getLookupKey() {
        return lookupKey;
    }

    public void setLookupKey(String lookupKey) {
        this.lookupKey = lookupKey;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }

    @Override
    public String toString() {
        return "ContactEntity{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", sortKey='" + sortKey + '\'' +
                ", contactId=" + contactId +
                ", lookupKey='" + lookupKey + '\'' +
                ", firstChar='" + firstChar + '\'' +
                '}';
    }
}
