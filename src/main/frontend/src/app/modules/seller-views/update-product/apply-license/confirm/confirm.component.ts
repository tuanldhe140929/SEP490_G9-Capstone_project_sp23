import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ProductService } from '../../../../../services/product.service';

@Component({
  selector: 'app-confirm',
  templateUrl: './confirm.component.html',
  styleUrls: ['./confirm.component.css']
})
export class ConfirmComponent {
  constructor(private productService: ProductService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialogRef: MatDialogRef<ConfirmComponent>) {

  }

  confirm() {
    this.productService.applyLicense(this.data.productId, this.data.licenseId).subscribe(
      (data) => {
        this.dialogRef.close();
      },
      (error) => {

      }
    );
  }

  close() {
    this.dialogRef.close();
  }
}
