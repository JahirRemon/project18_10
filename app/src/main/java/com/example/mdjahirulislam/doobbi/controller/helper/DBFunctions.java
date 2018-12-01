package com.example.mdjahirulislam.doobbi.controller.helper;

import android.util.Log;

import com.example.mdjahirulislam.doobbi.model.requestModel.InsertOrderDataModel;
import com.example.mdjahirulislam.doobbi.model.requestModel.InsertOrderHistoryDBModel;
import com.example.mdjahirulislam.doobbi.model.requestModel.InsertUserDataModel;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public  class DBFunctions {
    private static final String TAG = DBFunctions.class.getSimpleName();
    private static Realm mRealm = Realm.getDefaultInstance( );

    public static void addOrderHistory(InsertOrderHistoryDBModel model, String uniqueID){
        mRealm.beginTransaction();

        InsertOrderHistoryDBModel insertOrderHistoryDBModel = mRealm.createObject( InsertOrderHistoryDBModel.class ,uniqueID);
        insertOrderHistoryDBModel.setUserID( model.getUserID() );
        insertOrderHistoryDBModel.setItemID( model.getItemID() );
        insertOrderHistoryDBModel.setServiceID( model.getServiceID() );
        insertOrderHistoryDBModel.setServiceName( model.getServiceName() );
        insertOrderHistoryDBModel.setItemQuantity( model.getItemQuantity() );
        insertOrderHistoryDBModel.setTotalPrice( model.getTotalPrice() );

        mRealm.commitTransaction();
        Log.d( TAG, "onResume: id--> "+uniqueID+" \nData not found new data inset " + insertOrderHistoryDBModel.toString() );

    }

    public static void updateOrderHistory(InsertOrderHistoryDBModel model, String uniqueID){
        InsertOrderHistoryDBModel updateModel = mRealm.where( InsertOrderHistoryDBModel.class ).equalTo( "_id",uniqueID).findFirst();

        mRealm.beginTransaction();
        updateModel.setUserID( model.getUserID() );
        updateModel.setItemID( model.getItemID() );
        updateModel.setServiceID( model.getServiceID() );
        updateModel.setServiceName( model.getServiceName() );
        updateModel.setItemQuantity( model.getItemQuantity() );
        updateModel.setTotalPrice( model.getTotalPrice() );
//                                    InsertOrderHistoryDBModel update = mRealm.copyToRealm( getResultServiceID );
        mRealm.commitTransaction();
        Log.d( TAG, "onTouch: find serviceId----> update history---> " + updateModel.toString() );

    }

    public static int getAllOrderHistoryTotalPrice() {
        int totalPrice = 0;
        RealmResults<InsertOrderHistoryDBModel> results = mRealm.where( InsertOrderHistoryDBModel.class ).findAll();

        mRealm.beginTransaction();

        Log.d( TAG, "getAllOrderHistoryTotalPrice: size---> " + results.size() );
        for (int i = 0; i < results.size(); i++) {
            totalPrice += Integer.parseInt( results.get( i ).getTotalPrice() );
            Log.d( TAG, "getAllOrderHistoryTotalPrice: totalPrice---->" + totalPrice );
            Log.d( TAG, "getAllOrderHistoryTotalPrice: rowDetails-----> " + results.get( i ).toString() );
        }
        mRealm.commitTransaction();
        return totalPrice;
    }

    public static int getAllOrderHistoryTotalItems() {
        int totalItems = 0;
        RealmResults<InsertOrderHistoryDBModel> results = mRealm.where( InsertOrderHistoryDBModel.class ).findAll();

        mRealm.beginTransaction();

        Log.d( TAG, "getAllOrderHistoryTotalItems: size---> " + results.size() );
        for (int i = 0; i < results.size(); i++) {
            totalItems += Integer.parseInt( results.get( i ).getItemQuantity() );
            Log.d( TAG, "getAllOrderHistoryTotalItems: totalPrice---->" + totalItems );
            Log.d( TAG, "getAllOrderHistoryTotalItems: rowDetails-----> " + results.get( i ).toString() );
        }
        mRealm.commitTransaction();
        return totalItems;
    }
    public static ArrayList<InsertOrderDataModel> getAllOrderHistoryList() {
        ArrayList<InsertOrderDataModel> dataModels = new ArrayList<>(  );
        RealmResults<InsertOrderHistoryDBModel> results = mRealm.where( InsertOrderHistoryDBModel.class ).findAll();

        mRealm.beginTransaction();

        Log.d( TAG, "getAllOrderHistoryList: size---> " + results.size() );
        for (int i = 0; i < results.size(); i++) {
            dataModels.add( new InsertOrderDataModel( results.get( i ).getItemID(),
                    results.get( i ).getServiceID(),results.get( i ).getItemQuantity(),results.get( i ).getTotalPrice() ) );
            Log.d( TAG, "getAllOrderHistoryList: rowDetails-----> " + results.get( i ).toString() );
        }
        mRealm.commitTransaction();
        return dataModels;
    }

    public static InsertUserDataModel getUserInformation() {
        InsertUserDataModel userDataModel = new InsertUserDataModel(  );

        InsertUserDataModel result = mRealm.where( InsertUserDataModel.class ).findFirst();

        mRealm.beginTransaction();
        userDataModel = result;
        Log.d( TAG, "getUserInformation: "+userDataModel.toString() );

        mRealm.commitTransaction();
        return userDataModel;
    }
}
