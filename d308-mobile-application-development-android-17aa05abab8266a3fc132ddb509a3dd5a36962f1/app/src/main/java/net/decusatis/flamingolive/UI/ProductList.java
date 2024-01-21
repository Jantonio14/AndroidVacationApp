package net.decusatis.flamingolive.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.decusatis.flamingolive.R;
import net.decusatis.flamingolive.database.Repository;
import net.decusatis.flamingolive.entities.Part;
import net.decusatis.flamingolive.entities.Product;

import java.util.List;

public class ProductList extends AppCompatActivity {
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductList.this, ProductDetails.class);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView=findViewById(R.id.recyclerview);
        repository=new Repository(getApplication());
        List<Product> allProducts=repository.getmAllProducts();
        final ProductAdapter productAdapter=new ProductAdapter(this);
        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter.setProducts(allProducts);
        //System.out.println(getIntent().getStringExtra("test"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_list, menu);
        return true;

    }
    @Override
    protected void onResume() {

        super.onResume();
        List<Product> allProducts=repository.getmAllProducts();
        RecyclerView recyclerView=findViewById(R.id.recyclerview);
        final ProductAdapter productAdapter=new ProductAdapter(this);
        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter.setProducts(allProducts);

        //Toast.makeText(ProductDetails.this,"refresh list",Toast.LENGTH_LONG).show();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mysample) {
            repository=new Repository(getApplication());
            //Toast.makeText(ProductList.this,"put in sample data",Toast.LENGTH_LONG).show();
            Product product = new Product(0, "bicycle", 100.0);
            repository.insert(product);
            product = new Product(0, "tricycle", 100.0);
            repository.insert(product);
            Part part=new Part(0,"wheel",10,1);
            repository.insert(part);
            part=new Part(0,"pedal",10,1);
            repository.insert(part);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            //Intent intent=new Intent(ProductList.this,ProductDetails.class);
            //startActivity(intent);
            return true;
        }
        return true;
    }

}