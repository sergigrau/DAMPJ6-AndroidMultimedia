package edu.fje.dam2;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Classe que hereta de fragment
 *
 * @author sergi.grau@fje.edu
 * @version 5.0 27.01.2020
 * 
 */
public class M17_DetallFragmentActivity extends Fragment {

	Button botoEntrada;
	EditText textEntrada;
	OnNomAfegitListener callback;

	public void setOnNomAfegitListener(OnNomAfegitListener callback) {
		this.callback = callback;
	}

	// This interface can be implemented by the Activity, parent Fragment,
	// or a separate test implementation.
	 interface OnNomAfegitListener {
		void onNomAfegit(String nom);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.m17_fragment_detall,
				container, false);

		botoEntrada = view.findViewById(R.id.botoEntrada);
		textEntrada= view.findViewById(R.id.textEntrada);

		botoEntrada.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			callback.onNomAfegit(textEntrada.getText().toString());
			}
		});
		return view;
	}

}
