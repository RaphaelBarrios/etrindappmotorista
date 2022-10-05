package br.com.etrind.etrindappmotorista.Entity.Result.DistMatrix;

import java.util.ArrayList;

public class DistanceMatrixResult {
    public DistanceMatrixResult(){
        DestinationAddresses = new ArrayList<String>();
        OriginAddresses = new ArrayList<String>();
        Elements = new ArrayList<DistanceMatrixElementResult>();
    }
    public ArrayList<String> DestinationAddresses;
    public ArrayList<String> OriginAddresses;
    public ArrayList<DistanceMatrixElementResult> Elements;
    public String Status;

    public int ObterTempoTotalMinutos(){
        if(this.Elements == null) return 0;

        int tempoTotal = 0;
        for (DistanceMatrixElementResult element : this.Elements){
            if(element.duration != null) {
                tempoTotal += element.duration.value;
            }
        }
        return (tempoTotal / 60);
    }
}



