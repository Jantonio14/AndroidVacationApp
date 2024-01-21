package net.decusatis.flamingolive.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.decusatis.flamingolive.R;
import net.decusatis.flamingolive.database.Repository;
import net.decusatis.flamingolive.entities.Part;
import net.decusatis.flamingolive.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDetails extends AppCompatActivity {
    String name;
    double price;
    int productID;
    EditText editName;
    EditText editPrice;
    Repository repository;
    Product currentProduct;
    int numParts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        FloatingActionButton fab=findViewById(R.id.floatingActionButton2);

        editName=findViewById(R.id.titletext);
        editPrice=findViewById(R.id.pricetext);
        productID = getIntent().getIntExtra("id", -1);
        name = getIntent().getStringExtra("name");
        price = getIntent().getDoubleExtra("price", 0.0);
        editName.setText(name);
        editPrice.setText(Double.toString(price));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProductDetails.this, PartDetails.class);
                intent.putExtra("prodID", productID);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.partrecyclerview);
        repository = new Repository(getApplication());
        final PartAdapter partAdapter = new PartAdapter(this);
        recyclerView.setAdapter(partAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Part> filteredParts = new ArrayList<>();
        for (Part p : repository.getAllParts()) {
            if (p.getProductID() == productID) filteredParts.add(p);
        }
        partAdapter.setParts(filteredParts);

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_productdetails, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.productsave){
            Product product;
            if (productID==-1){
                if (repository.getmAllProducts().size() == 0) productID = 1;
                else productID = repository.getmAllProducts().get(repository.getmAllProducts().size() - 1).getProductID() + 1;
                product = new Product(productID, editName.getText().toString(), Double.parseDouble(editPrice.getText().toString()));
                repository.insert(product);
                this.finish();
            }
            else{
                product = new Product(productID, editName.getText().toString(), Double.parseDouble(editPrice.getText().toString()));
                repository.update(product);
                this.finish();
            }
        }
        if(item.getItemId()== R.id.productdelete){
            for(Product prod:repository.getmAllProducts()){
                if(prod.getProductID()==productID)currentProduct=prod;
            }
            numParts=0;
            for(Part part: repository.getAllParts()){
                if(part.getProductID()==productID)++numParts;
            }
            if(numParts==0){
                repository.delete(currentProduct);
                Toast.makeText(ProductDetails.this, currentProduct.getProductName() + " was deleted",Toast.LENGTH_LONG).show();
                ProductDetails.this.finish();
            }
            else{
                Toast.makeText(ProductDetails.this, "Can't delete a product with parts",Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }
    @Override
    protected void onResume() {

        super.onResume();
        RecyclerView recyclerView=findViewById(R.id.partrecyclerview);
        final PartAdapter partAdapter = new PartAdapter(this);
        recyclerView.setAdapter(partAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Part> filteredParts = new ArrayList<>();
        for (Part p : repository.getAllParts()) {
            if (p.getProductID() == productID) filteredParts.add(p);
        }
        partAdapter.setParts(filteredParts);

        //Toast.makeText(ProductDetails.this,"refresh list",Toast.LENGTH_LONG).show();
    }
}