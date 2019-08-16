//package io.andref.rx.widget.example;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.widget.Button;
//import android.widget.Toast;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//import io.andref.rx.widgets.ExpandableButtonGroup;
//import io.andref.rx.widgets.ListViewCard;
//import io.reactivex.disposables.CompositeDisposable;
//
//public class MainActivity extends AppCompatActivity
//{
//    CompositeDisposable disposable = new CompositeDisposable();
//
//    ExpandableButtonGroup mExpandableButtonGroup;
//
//    ListViewCard mListViewCard;
//    Button mListViewCardButton1;
//    Button mListViewCardButton2;
//    Button mListViewCardButton3;
//    Button mListViewCardButton4;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//
//        /* ExpandableButtonGroup */
//
//        List<ExpandableButtonGroup.Item> expandableButtonGroupItems = new ArrayList<>();
//
//        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("Restaurants", R.drawable.ic_local_dining_black_24dp));
//        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("Gas Stations", R.drawable.ic_local_gas_station_black_24dp));
//        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("ATMs", R.drawable.ic_local_atm_black_24dp));
//        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("Coffee", R.drawable.ic_local_cafe_black_24dp));
//        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("Pharmacies", R.drawable.ic_local_pharmacy_black_24dp));
//        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("Grocery Stores", R.drawable.ic_local_grocery_store_black_24dp));
//        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("Hotels", R.drawable.ic_local_hotel_black_24dp));
//        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("Bars", R.drawable.ic_local_bar_black_24dp));
//        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("Department Stores", R.drawable.ic_local_mall_black_24dp));
//        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("Post Offices", R.drawable.ic_local_post_office_black_24dp));
//        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("Parking", R.drawable.ic_local_parking_black_24dp));
//
//        expan = (ExpandableButtonGroup) findViewById(R.id.expandableButtonGroup);
//        expan.setItems(expandableButtonGroupItems);
//
//
//        /* ListViewCard **/
//
//        List<ListViewCard.Item> listViewCardItems = new ArrayList<>();
//
//        listViewCardItems.add(new ListViewCard.Item("(804) 555-1234", "Home Phone", R.drawable.ic_phone_black_24dp, R.drawable.ic_sms_black_24dp));
//        listViewCardItems.add(new ListViewCard.Item("andrefio@example.com", "Office E-mail", R.drawable.ic_email_black_24dp, 0));
//        listViewCardItems.add(new ListViewCard.Item("(804) 555-1234", "Home Phone", R.drawable.ic_phone_black_24dp, R.drawable.ic_sms_black_24dp));
//
//        listViewCard = (ListViewCard) findViewById(R.id.listViewCard);
//        listViewCard.setItems(listViewCardItems);
//
//        mListViewCardButton1 = (Button) findViewById(R.id.listViewCardButton1);
//        mListViewCardButton2 = (Button) findViewById(R.id.listViewCardButton2);
//        mListViewCardButton3 = (Button) findViewById(R.id.listViewCardButton3);
//        mListViewCardButton4 = (Button) findViewById(R.id.listViewCardButton4);
//    }
//
//    @Override
//    protected void onResume()
//    {
//        super.onResume();
//
//        /* ExpandableButtonGroup */
//
//                expan.lessItemClicks()
//                        .subscribe(new Action1<Void>()
//                        {
//                            @Override
//                            public void call(Void aVoid)
//                            {
//                                expan.showLessItems();
//                            }
//                        })
//        );
//
//        mSubscriptions.add(
//                expan.moreItemClicks()
//                        .subscribe(new Action1<Void>()
//                        {
//                            @Override
//                            public void call(Void aVoid)
//                            {
//                                expan.showMoreItems();
//                            }
//                        })
//        );
//
//        mSubscriptions.add(
//                expan.itemClicks()
//                        .subscribe(new Action1<ExpandableButtonGroup.Item>()
//                        {
//                            @Override
//                            public void call(ExpandableButtonGroup.Item item)
//                            {
//                                Toast.makeText(getBaseContext(), item.getText(), Toast.LENGTH_SHORT).show();
//                            }
//                        })
//        );
//
//
//        /* ListViewCard **/
//
//        mSubscriptions.add(
//                listViewCard.itemClicks()
//                        .subscribe(new Action1<ListViewCard.Item>()
//                        {
//                            @Override
//                            public void call(ListViewCard.Item item)
//                            {
//                                Toast.makeText(getBaseContext(), item.getLine1(), Toast.LENGTH_SHORT).show();
//                            }
//                        })
//        );
//
//        mSubscriptions.add(
//                listViewCard.iconClicks()
//                        .subscribe(new Action1<ListViewCard.Item>()
//                        {
//                            @Override
//                            public void call(ListViewCard.Item item)
//                            {
//                                Toast.makeText(getBaseContext(), "Icon Clicked: " + item.getLine1(), Toast.LENGTH_SHORT).show();
//                            }
//                        })
//        );
//
//        mSubscriptions.add(
//                listViewCard.buttonClicks()
//                        .subscribe(new Action1<Void>()
//                        {
//                            @Override
//                            public void call(Void aVoid)
//                            {
//                                Toast.makeText(getBaseContext(), "Button Clicked", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//        );
//
//        mSubscriptions.add(
//                RxView.clicks(mListViewCardButton1)
//                        .subscribe(new Action1<Void>()
//                        {
//                            @Override
//                            public void call(Void aVoid)
//                            {
//                                listViewCard.addItem(new ListViewCard.Item("additional@example.com", "Other E-mail", R.drawable.ic_email_black_24dp, R.drawable.ic_sms_black_24dp));
//                            }
//                        })
//        );
//
//        mSubscriptions.add(
//                RxView.clicks(mListViewCardButton2)
//                        .subscribe(new Action1<Void>()
//                        {
//                            @Override
//                            public void call(Void aVoid)
//                            {
//                                int size = listViewCard.getItems().size();
//                                if (size > 0)
//                                {
//                                    listViewCard.removeItem(size - 1);
//                                }
//                            }
//                        })
//        );
//
//        mSubscriptions.add(
//                RxView.clicks(mListViewCardButton3)
//                        .subscribe(new Action1<Void>()
//                        {
//                            @Override
//                            public void call(Void aVoid)
//                            {
//                                listViewCard.hideButton();
//                            }
//                        })
//        );
//
//        mSubscriptions.add(
//                RxView.clicks(mListViewCardButton4)
//                        .subscribe(new Action1<Void>()
//                        {
//                            @Override
//                            public void call(Void aVoid)
//                            {
//                                listViewCard.showButton();
//                            }
//                        })
//        );
//    }
//
//    @Override
//    protected void onPause()
//    {
//        super.onPause();
//        mSubscriptions.unsubscribe();
//    }
//}
