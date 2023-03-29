import { Component, Inject, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ProductService } from '../../../../services/product.service';

@Component({
  selector: 'app-delete-product',
  templateUrl: './delete-product.component.html',
  styleUrls: ['./delete-product.component.css']
})
export class DeleteProductComponent {

  @ViewChild('infoModal', { static: false }) private infoModal: any;


  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { productId: number },
    private dialogRef: MatDialogRef<DeleteProductComponent>,
    private productService: ProductService) {

  }

  onDelete(id: number) {
    this.productService.deleteProduct(this.data.productId).subscribe(
      data => {
        console.log(data);
        this.dialogRef.close(true);
      },
      error => {
        if (error.status === 400) {
          this.dialogRef.close(false);
        }
      }
    )
  }
}
