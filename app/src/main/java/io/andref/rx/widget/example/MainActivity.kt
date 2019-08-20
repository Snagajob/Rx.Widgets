package io.andref.rx.widget.example


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import io.andref.rx.widgets.ExpandableButtonGroup
import io.andref.rx.widgets.ListViewCard
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        /* ExpandableButtonGroup */
        val expandableButtonGroupItems = ArrayList<ExpandableButtonGroup.Item<*>>()

        expandableButtonGroupItems.add(ExpandableButtonGroup.Item<Any>("Restaurants", R.drawable.ic_local_dining_black_24dp))
        expandableButtonGroupItems.add(ExpandableButtonGroup.Item<Any>("Gas Stations", R.drawable.ic_local_gas_station_black_24dp))
        expandableButtonGroupItems.add(ExpandableButtonGroup.Item<Any>("ATMs", R.drawable.ic_local_atm_black_24dp))
        expandableButtonGroupItems.add(ExpandableButtonGroup.Item<Any>("Coffee", R.drawable.ic_local_cafe_black_24dp))
        expandableButtonGroupItems.add(ExpandableButtonGroup.Item<Any>("Pharmacies", R.drawable.ic_local_pharmacy_black_24dp))
        expandableButtonGroupItems.add(ExpandableButtonGroup.Item<Any>("Grocery Stores", R.drawable.ic_local_grocery_store_black_24dp))
        expandableButtonGroupItems.add(ExpandableButtonGroup.Item<Any>("Hotels", R.drawable.ic_local_hotel_black_24dp))
        expandableButtonGroupItems.add(ExpandableButtonGroup.Item<Any>("Bars", R.drawable.ic_local_bar_black_24dp))
        expandableButtonGroupItems.add(ExpandableButtonGroup.Item<Any>("Department Stores", R.drawable.ic_local_mall_black_24dp))
        expandableButtonGroupItems.add(ExpandableButtonGroup.Item<Any>("Post Offices", R.drawable.ic_local_post_office_black_24dp))
        expandableButtonGroupItems.add(ExpandableButtonGroup.Item<Any>("Parking", R.drawable.ic_local_parking_black_24dp))

        expandableButtonGroup.items = expandableButtonGroupItems


        /* ListViewCard **/

        val listViewCardItems = ArrayList<ListViewCard.Item<*>>()

        listViewCardItems.add(ListViewCard.Item<Any>("(804) 555-1234", "Home Phone", R.drawable.ic_phone_black_24dp, R.drawable.ic_sms_black_24dp))
        listViewCardItems.add(ListViewCard.Item<Any>("andrefio@example.com", "Office E-mail", R.drawable.ic_email_black_24dp, 0))
        listViewCardItems.add(ListViewCard.Item<Any>("(804) 555-1234", "Home Phone", R.drawable.ic_phone_black_24dp, R.drawable.ic_sms_black_24dp))

        listViewCard.items = listViewCardItems


    }





    @SuppressWarnings("CheckResult")
    override fun onResume() {
        super.onResume()

        /* ExpandableButtonGroup */

        expandableButtonGroup.lessItemClicks()
                .subscribe {
                    expandableButtonGroup.showLessItems()
                }.disposeWith(disposable)

        expandableButtonGroup.moreItemClicks()
                .subscribe {
                    expandableButtonGroup.showMoreItems()
                }.disposeWith(disposable)


        expandableButtonGroup.itemClicks()
                .subscribe {

                    Toast.makeText(baseContext, it.text, Toast.LENGTH_SHORT).show()
                }
                .disposeWith(disposable)


        /* ListViewCard **/
        listViewCard.itemClicks()
                .subscribe {
                    Toast.makeText(baseContext, it.line1, Toast.LENGTH_SHORT).show()
                }.disposeWith(disposable)


        listViewCard.iconClicks()
                .subscribe {
                    Toast.makeText(baseContext, "Icon Clicked: " + it.line1, Toast.LENGTH_SHORT).show()
                }.disposeWith(disposable)


        listViewCard.buttonClicks()
                .subscribe {
                    Toast.makeText(baseContext, "Button Clicked", Toast.LENGTH_SHORT).show()
                }.disposeWith(disposable)

        RxView.clicks(listViewCardButton1)
                .subscribe {
                    listViewCard.addItem(ListViewCard.Item<Any>("additional@example.com", "Other E-mail", R.drawable.ic_email_black_24dp, R.drawable.ic_sms_black_24dp))
                }.disposeWith(disposable)



        RxView.clicks(listViewCardButton2)
                .subscribe {
                    val size = listViewCard.items.size
                    if (size > 0) {
                        listViewCard.removeItem(size - 1)
                    }
                }.disposeWith(disposable)


        RxView.clicks(listViewCardButton3)
                .subscribe {
                    listViewCard.hideButton()
                }.disposeWith(disposable)


        RxView.clicks(listViewCardButton4)
                .subscribe {

                    listViewCard.showButton()
                }.disposeWith(disposable)
    }

    override fun onPause() {
        super.onPause()
        disposable.clear()
    }
}
