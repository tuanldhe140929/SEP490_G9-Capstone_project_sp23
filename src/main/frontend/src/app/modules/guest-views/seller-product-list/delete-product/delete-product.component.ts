import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ProductService } from '../../../../services/product.service';

@Component({
  selector: 'app-delete-product',
  templateUrl: './delete-product.component.html',
  styleUrls: ['./delete-product.component.css']
})
export class DeleteProductComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { productId: number},
    private productService: ProductService) {

  }

  onDelete(id: number) {
    this.productService.deleteProduct(this.data.productId).subscribe(
      data => {
        console.log(data);
      
      },
      error => {
        console.log(error);
      }
    )
  }
}
