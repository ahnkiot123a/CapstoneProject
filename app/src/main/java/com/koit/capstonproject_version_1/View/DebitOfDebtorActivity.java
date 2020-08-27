package com.koit.capstonproject_version_1.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Controller.DebitOfDebtorController;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.helper.Helper;

import de.hdodenhof.circleimageview.CircleImageView;

public class DebitOfDebtorActivity extends AppCompatActivity {

    public static final String ITEM_DEBTOR = "ITEM_DEBTOR";
    private RecyclerView rvDebtPaymentHistory;
    private TextView tvDebtTotal, tvPayAmountTotal, tvDebtAmountTotal, tvDebtorName, tvDebtorPhone, tvDebtorAddress, tvFirstName;
    private CircleImageView ivAvatar;
    private ProgressBar pbDebit;
    private Button btnPayment;

    private Debtor currentDebtor;
    private DebitOfDebtorController debitOfDebtorController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_debit_payment);

        initView();
        debitOfDebtorController = new DebitOfDebtorController(this);
        currentDebtor = getCurrentDebtor();

        debitOfDebtorController.checkDebtMoneyAndButtonView(currentDebtor, btnPayment);

        Helper.getInstance().setImage(ivAvatar, tvFirstName, currentDebtor.getFullName().charAt(0));
        setInformationDebtor();
        setDebtPaymentList();


    }

    private void setInformationDebtor() {
        if(currentDebtor != null){
            tvDebtorName.setText(currentDebtor.getFullName());
            tvDebtorPhone.setText(currentDebtor.getPhoneNumber());
            tvDebtorAddress.setText(currentDebtor.getAddress());
            long debtAmountTotal = currentDebtor.getRemainingDebit();
            tvDebtAmountTotal.setText(Money.getInstance().formatVN(debtAmountTotal) + " đ");
            tvPayAmountTotal.setText("0 đ");
            tvDebtTotal.setText(Money.getInstance().formatVN(debtAmountTotal) + " đ");
            pbDebit.setMax((int) debtAmountTotal);
            pbDebit.setProgress(0);
        }
    }

    private void setDebtPaymentList() {
        debitOfDebtorController.setDebtPaymentList(currentDebtor, rvDebtPaymentHistory, tvPayAmountTotal, tvDebtTotal, tvDebtAmountTotal, pbDebit);
    }

    private Debtor getCurrentDebtor() {
        Debtor debtor;
        Intent intent = getIntent();
        debtor = (Debtor) intent.getSerializableExtra(ITEM_DEBTOR);
        return debtor;
    }

    private void initView() {
        rvDebtPaymentHistory = findViewById(R.id.rvDebtPaymentHistory);
        tvDebtTotal = findViewById(R.id.tvDebtTotal);
        tvPayAmountTotal = findViewById(R.id.tvPayAmountTotal);
        tvDebtAmountTotal = findViewById(R.id.tvDebtAmountTotal);
        tvDebtorName = findViewById(R.id.tvDebtorName);
        tvDebtorPhone = findViewById(R.id.tvDebtorPhone);
        tvDebtorAddress = findViewById(R.id.tvDebtorAddress);
        ivAvatar = findViewById(R.id.imgAvatar);
        pbDebit = findViewById(R.id.pbDebit);
        tvFirstName = findViewById(R.id.tvFirstName);
        btnPayment = findViewById(R.id.btnPayment);

        //set title in toolbar
        Toolbar toolbar = findViewById(R.id.toolbarGeneral);
        TextView tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Thông tin nợ");


    }

    public void back(View view){
        onBackPressed();
    }

    public void callEditDebtorActivity(View view) {
        Intent intent = new Intent(this, EditDebtorActivity.class);
        intent.putExtra(ITEM_DEBTOR, currentDebtor);
        startActivity(intent);
        finish();
    }

    public void callInputPayDebtMoney(View view) {
        Intent intent = new Intent(this, InputPayDebtMoneyActivity.class);
        intent.putExtra(ITEM_DEBTOR, currentDebtor);
        startActivity(intent);
        finish();
    }

    public void callOrderDebtorActivity(View view) {
        Intent intent = new Intent(this, DebitOrderListActivity.class);
        intent.putExtra(ITEM_DEBTOR, currentDebtor);
        startActivity(intent);
    }
}