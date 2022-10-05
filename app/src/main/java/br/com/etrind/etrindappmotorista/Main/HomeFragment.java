package br.com.etrind.etrindappmotorista.Main;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.etrind.etrindappmotorista.R;

public class HomeFragment extends Fragment {
    @SuppressWarnings("FieldCanBeLocal")
    private Context ctx;
    public final String Cnh;
    private final String Nome;

    public HomeFragment(String cnh, String nome) {

        this.Cnh = cnh;
        this.Nome = nome;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ctx =  getActivity();
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView tvHomeCnh = view.findViewById(R.id.tvHomeCnh);
        tvHomeCnh.setText(String.format(ctx.getString(R.string.HomeFragment_tvHomeCnh), this.Nome, this.Cnh));

        return view;
    }
}