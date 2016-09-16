package com.canmeizhexue.common.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.canmeizhexue.common.entity.contact.ContactEntity;
import com.canmeizhexue.common.utils.pinyin.PinYinUtil;

import java.io.InputStream;

/**联系人
 * Created by silence on 2016/9/16.
 */
public class PhoneBookUtil {
    public static final String[] DEFAULT_SELECTION={BaseColumns._ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.TYPE,
            ContactsContract.CommonDataKinds.Phone.LABEL, ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY };

    /**** 联系人 是否还要加上lookupkey***/
    public static final String SELECTION_FOR_SEARCH_ALL_CONTACTS = new StringBuilder()//
            .append("(").append(ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY)//
            .append(" like ?").append(" OR ").append(ContactsContract.CommonDataKinds.Phone.NUMBER)//
            .append(" like ?").append(" OR ").append(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)//
            .append(" like ?").append(")").toString();
    /**
     * 获取所有联系人的手机号码
     *
     * @param context
     * @return
     */
    public static Cursor getMobilePhoneCursor(Context context,String searchKey) {
        final String[] projection = DEFAULT_SELECTION;
        String select ;
        String[] selectionArgs;
        if (TextUtils.isEmpty(searchKey)) {
            select = "((" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                    + " NOTNULL ) AND  (" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                    + " != '') AND  (" + ContactsContract.CommonDataKinds.Phone.NUMBER + " != ''))";
            selectionArgs=null;
        }else{
            select = SELECTION_FOR_SEARCH_ALL_CONTACTS;
            String selargs = "%" + searchKey + "%";
            selectionArgs=new String[] {selargs, selargs, selargs };
        }

        return context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
                select, selectionArgs, ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY+" COLLATE LOCALIZED ASC");
    }
    public static ContactEntity parseCursor(Cursor cursor){
        if(cursor!=null){
            ContactEntity contactEntity = new ContactEntity();
            int idIndex = cursor.getColumnIndex(BaseColumns._ID);
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int lookupIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY);
            int contactIdIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
            int sortkeyIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY);
            String displayName = cursor.getString(nameIndex);
            String number = cursor.getString(numberIndex);
            String sorkey = cursor.getString(sortkeyIndex);
            int contactId = cursor.getInt(contactIdIndex);
/*                if (TextUtils.isEmpty(number) || TextUtils.isEmpty(sorkey)) {
                    return null;
                }*/
            displayName = displayName.trim();
            // 只需要手机号码

            contactEntity.setName(displayName);
            contactEntity.setPhone(number);
            contactEntity.setContactId(contactId);
            contactEntity.setLookupKey(cursor.getString(lookupIndex));
            contactEntity.setSortKey(sorkey);
            contactEntity.setFirstChar(getSortChar(PinYinUtil.getPinYin(sorkey)));
            return contactEntity;
        }
        return null;
    }
    /****
     * 取得：排序的首字母
     *
     * @param sorkey
     * @return
     */
    public static String getSortChar(String sorkey) {
        if (TextUtils.isEmpty(sorkey)) {
            return "#";
        }
        char sortkeychar = sorkey.substring(0, 1).toUpperCase().charAt(0);
        if (sortkeychar < 'A' || sortkeychar > 'Z') {
            return "#";
        }
        return String.valueOf(sortkeychar);
    }
    /**
     * 获取联系人头像图片
     * */
    public static Bitmap getContactPhoto(Context context, String strPhoneNumber) {
        final String[] projection = { ContactsContract.Contacts._ID };
        Uri uriNumber2Contacts = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, strPhoneNumber);
        // .parse("content://com.android.contacts/data/phones/filter/" +
        // strPhoneNumber);
        Cursor cursorCantacts = null;
        try {
            cursorCantacts = context.getContentResolver().query(uriNumber2Contacts, projection, null, null, null);
            int contactId = -1;
            if (cursorCantacts != null) {
                if (cursorCantacts.moveToFirst()) {
                    contactId = cursorCantacts.getInt(cursorCantacts.getColumnIndex(ContactsContract.Contacts._ID));
                }
                cursorCantacts.close();
                cursorCantacts = null;
            }
            if (contactId > 0) {
                return getContactPhoto(context, contactId);
            }
        } catch (Throwable tr) {
            tr.printStackTrace();
        } finally {
            if (cursorCantacts != null && !cursorCantacts.isClosed()) {
                cursorCantacts.close();
            }
        }

        return null;
    }

    /**
     * 获取联系人头像图片
     * */
    public static Bitmap getContactPhotoByKey(Context context, String key) {
		/*
		 * final String[] projection = {BaseColumns._ID};
		 *
		 * Cursor cursorCantacts
		 * =context.getContentResolver().query(ContactsContract
		 * .Contacts.CONTENT_URI, projection,
		 * ContactsContract.Contacts.LOOKUP_KEY + "=?", new String[]{key},
		 * null); int contactId =-1; if (cursorCantacts!=null) { if
		 * (cursorCantacts.moveToFirst()){ contactId = cursorCantacts.getInt(0);
		 * } cursorCantacts.close(); } if (contactId>0){ return
		 * getContactPhoto(context,contactId); } return null;
		 */

        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, key);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), uri);
        if (input != null) {
            Bitmap btContactImage = BitmapFactory.decodeStream(input);
            return btContactImage;
        }
        return null;
    }

    /**
     * 获取联系人头像图片
     * */
    public static Bitmap getContactPhoto(Context context, int contactId) {
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), uri);
        if (input != null) {
            Bitmap btContactImage = BitmapFactory.decodeStream(input);
            return btContactImage;
        }
        return null;
    }
}
