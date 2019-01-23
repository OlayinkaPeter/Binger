package ng.max.binger.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.max.binger.R;
import ng.max.binger.data.ProductionCompany;

public class ProductionCompanyAdapter extends RecyclerView.Adapter<ProductionCompanyAdapter.ProductionCompanyViewHolder> {
    private List<ProductionCompany> productionCompanyList;
    private int rowLayout;
    private Context context;


    public static class ProductionCompanyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.companyLogo)
        ImageView productionCompanyLogo;

        public ProductionCompanyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public ProductionCompanyAdapter(List<ProductionCompany> productionCompanyList, int rowLayout, Context context) {
        this.productionCompanyList = productionCompanyList;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public ProductionCompanyAdapter.ProductionCompanyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ProductionCompanyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ProductionCompanyViewHolder holder, final int position) {
        Glide.with(context).load("https://image.tmdb.org/t/p/w500/" + productionCompanyList.get(position).getLogoPath()).into(holder.productionCompanyLogo);
    }

    @Override
    public int getItemCount() {
        return productionCompanyList.size();
    }
}
