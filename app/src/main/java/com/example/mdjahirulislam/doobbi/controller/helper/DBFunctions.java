package com.example.mdjahirulislam.doobbi.controller.helper;

import android.util.Log;

import com.example.mdjahirulislam.doobbi.model.requestModel.InsertOrderHistoryDBModel;

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

        Log.d( TAG, "onCreate: size---> " + results.size() );
        for (int i = 0; i < results.size(); i++) {
            totalPrice += Integer.parseInt( results.get( i ).getTotalPrice() );
            Log.d( TAG, "onCreate: totalPrice---->" + totalPrice );
            Log.d( TAG, "onCreate: rowDetails-----> " + results.get( i ).toString() );
        }
        mRealm.commitTransaction();
        return totalPrice;
    }
}
