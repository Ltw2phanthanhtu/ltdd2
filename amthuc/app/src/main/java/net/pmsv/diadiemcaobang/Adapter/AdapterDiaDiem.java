package net.pmsv.diadiemcaobang.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.share.widget.ShareDialog;

import net.pmsv.diadiemcaobang.DTO.DiaDiemDTO;
import net.pmsv.diadiemcaobang.DiaDiemDetailActivity;
import net.pmsv.diadiemcaobang.GocChupActivity;
import net.pmsv.diadiemcaobang.R;

import java.util.List;


/**
 * Created by may38 on 5/29/2017.
 */

public class AdapterDiaDiem extends RecyclerView.Adapter<AdapterDiaDiem.ViewHolder> {
    private Context mContext;
    private List<DiaDiemDTO> diaDiemList;
    private ShareDialog shareDialog;
    private ImageView anhDaiDienDD;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tenDiaDiem, gocChup;
        public ImageView hinh;
        public ImageView overflow;

        public ViewHolder(View view) {
            super(view);
            tenDiaDiem = (TextView) view.findViewById(R.id.title);
            gocChup = (TextView) view.findViewById(R.id.count);
            hinh = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);

        }
    }


    public AdapterDiaDiem(Context mContext, List<DiaDiemDTO> diaDiemList) {
        this.mContext = mContext;
        this.diaDiemList = diaDiemList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dia_diem_card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DiaDiemDTO diaDiemDTO = diaDiemList.get(position);
        holder.tenDiaDiem.setText(diaDiemDTO.getTen());
        //holder.gocChup.setBackground(Drawable.createFromPath("R.drawable" + diaDiemDTO.getHinhdaidien()));
        if (diaDiemDTO.getSogocchup() < 0) {
            holder.gocChup.setText("Chưa có góc chụp");
        }
        holder.gocChup.setText(diaDiemDTO.getSogocchup() + " góc chụp");
        Resources resources = mContext.getResources();
        int resID = resources.getIdentifier(diaDiemDTO.getHinhdaidien(), "drawable", mContext.getPackageName());
        // loading album cover using Glide library
        Glide.with(mContext).load(resID).into(holder.hinh);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, diaDiemDTO);
            }
        });

        holder.hinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateIntent(v, diaDiemDTO);
            }
        });



    }

    public void animateIntent(View view, DiaDiemDTO diaDiemDTO) {
        Intent intent = new Intent(mContext, DiaDiemDetailActivity.class);
        intent.putExtra("dataDiaDiem", diaDiemDTO);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Get the transition name from the string
            String transitionName = mContext.getString(R.string.transition_string);
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)mContext,
                            view,   // Starting view
                            transitionName    // The String
                    );
            ActivityCompat.startActivity(mContext, intent, options.toBundle());
        }
        else {
            mContext.startActivity(intent);
        }
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, DiaDiemDTO diaDiemDTO) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new AdapterDiaDiem.MyMenuItemClickListener(diaDiemDTO));
        popup.show();


    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private DiaDiemDTO diaDiemDTO;

        public MyMenuItemClickListener(DiaDiemDTO diaDiemDTO) {
            this.diaDiemDTO = diaDiemDTO;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            DialogThongTin(diaDiemDTO, menuItem.getItemId());
            return false;
        }
    }

    private void DialogThongTin(DiaDiemDTO diadiem, int id) {
        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_thong_tin);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.textViewTitle);
        TextView txtThongTin = (TextView) dialog.findViewById(R.id.textViewThongTin);
        txtThongTin.setMovementMethod(new ScrollingMovementMethod());
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        switch (id) {
            case R.id.thongtin:
                txtTitle.setText(R.string.thong_tin);
                txtThongTin.setText(diadiem.getThongtin());
                dialog.show();
                break;
            case R.id.thoidiem:
                txtTitle.setText(R.string.thoi_diem);
                txtThongTin.setText(diadiem.getThoidiem());
                dialog.show();
                break;
            case R.id.duongdi:
                txtTitle.setText(R.string.duong_di);
                txtThongTin.setText(diadiem.getDuongdi());
                dialog.show();
                break;
            case R.id.gochup:
                if(diadiem.getSogocchup() > 0) {
                    Intent intent = new Intent(dialog.getContext(), GocChupActivity.class);
                    intent.putExtra("data", diadiem);
                    dialog.getContext().startActivity(intent);
                    return;
                }
                else {
                    Toast.makeText(mContext, "Không có góc chụp", Toast.LENGTH_LONG).show();
                    break;
                }
                case R.id.machnho:
                    txtTitle.setText(R.string.mach_nho);
                    txtThongTin.setText(diadiem.getMachnho());
                    dialog.show();
                    break;
            default:
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return diaDiemList.size();
    }


}
