package com.example.dzik;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private static final String TAG ="FK_ACTIVITY" ;
    Context context;
    Vector<String> opisy;
    Vector<String> data;
    Vector<String> status;
    Vector<String> miejsce;
    Vector<Integer> ileMlodych;
    Vector<Integer> ileStarych;
    Dialog mDialog;
    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2 ;
    RadioButton radioButton3 ;
    RadioButton radioButton4 ;
    RadioButton radioButton5;
    RadioButton radioButton6 ;
    EditText editText ;


    public HistoryAdapter(Context ct, Vector<String> description, Vector<String> date, Vector<String> state, Vector<String> place, Vector<Integer> ileYoung, Vector<Integer> ileOld){
        context=ct;
        opisy=description;
        data=date;
        status=state;
        miejsce=place;
        ileMlodych=ileYoung;
        ileStarych=ileOld;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
       View view = inflater.inflate(R.layout.history_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    holder.myData.setText(data.elementAt(position));
    holder.myStatus.setText("Status: "+status.elementAt(position));
    holder.myMiejsce.setText(miejsce.elementAt(position));
    holder.myIleStarych.setText(ileStarych.elementAt(position).toString());
    holder.myIleMlodych.setText(ileMlodych.elementAt(position).toString());

    holder.btnAnulujZgloszenie.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pokazDialogAnuluj();
        }
    });
    }

    private void pokazDialogAnuluj() {
        mDialog.setContentView(R.layout.dialog_anuluj);

        Button btnReturn = (Button) mDialog.findViewById(R.id.btnReturn_neg);
        Button btnGo = (Button) mDialog.findViewById(R.id.dialog_anuluj_but_anuluj);
        radioGroup = (RadioGroup) mDialog.findViewById(R.id.rg_powody);
        radioButton1 = (RadioButton) mDialog.findViewById(R.id.radio_anuluj1);
        radioButton2 = (RadioButton) mDialog.findViewById(R.id.radio_anuluj2);
        radioButton3 = (RadioButton) mDialog.findViewById(R.id.radio_anuluj3);
        radioButton4 = (RadioButton) mDialog.findViewById(R.id.radio_anuluj4);
        radioButton5 = (RadioButton) mDialog.findViewById(R.id.radio_anuluj5);
        radioButton6 = (RadioButton) mDialog.findViewById(R.id.radio_anuluj6);
        editText = (EditText) mDialog.findViewById(R.id.et_powod);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mDialog.dismiss();
            }
        });
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Przesyłanie na serwer zlecenia archiwizacji
                mDialog.dismiss();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.radio_anuluj6){
                    editText.setEnabled(true);
                } else{
                    editText.setEnabled(false);
                }
            }
        });

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Log.d(TAG, "PokazDialog: przed show");
        Log.d(TAG, "PokazDialog: pokazuje dialog:" + mDialog);
        mDialog.show();
    }

/*    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_anuluj1:
            case R.id.radio_anuluj2:
            case R.id.radio_anuluj3:
            case R.id.radio_anuluj4:
            case R.id.radio_anuluj5:
                if (checked)
                    editText.setEnabled(false);
                    break;
            case R.id.radio_anuluj6:
                if (checked)
                    editText.setEnabled(true);
                    break;
        }
    }*/

    @Override
    public int getItemCount() {
        return status.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView myOpis,myData,myStatus,myMiejsce,myIleMlodych,myIleStarych;
        Button btnAnulujZgloszenie;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myData = itemView.findViewById(R.id.tv_data);
            myStatus = itemView.findViewById(R.id.tv_status);
            myMiejsce = itemView.findViewById(R.id.tv_miejsce);
            myIleMlodych = itemView.findViewById(R.id.tv_ileMalych);
            myIleStarych = itemView.findViewById(R.id.tv_ileDuzych);
            btnAnulujZgloszenie=itemView.findViewById(R.id.btn_anuluj);
            mDialog = new Dialog(context);
        }
    }
}
