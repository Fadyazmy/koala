package com.tree.koala.Models;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.tree.koala.R;

import java.util.List;

/**
 * Created by Elad on 9/18/2016.
 */
public class FileItem extends AbstractItem<FileItem, FileItem.ViewHolder> {
  String name;
  String type;



  public FileItem(String name, String type) {
    this.name = name;
    this.type = type;
  }

  @Override
  public int getType() {
    return 0;
  }

  @Override
  public int getLayoutRes() {
    return R.layout.file_item;
  }

  @Override
  public void bindView(ViewHolder holder, List payloads) {
    super.bindView(holder, payloads);

    holder.fileName.setText(name);
//    holder.fileType.setText(type);
  }

  //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
  public static class ViewHolder extends RecyclerView.ViewHolder {
    protected ImageView fileIcon;
    protected TextView fileName;
    protected TextView fileType;

    public ViewHolder(View view) {
      super(view);
      this.fileName = (TextView) view.findViewById(R.id.file_item_title);
    }
  }
}
