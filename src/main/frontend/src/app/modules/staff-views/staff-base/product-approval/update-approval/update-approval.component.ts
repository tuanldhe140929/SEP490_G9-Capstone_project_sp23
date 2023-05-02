import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-update-approval',
  templateUrl: './update-approval.component.html',
  styleUrls: ['./update-approval.component.css']
})
export class UpdateApprovalComponent {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: {productId: number, productName: string, version: string},
    private dialogRef: MatDialogRef<UpdateApprovalComponent>, 
    private productService: ProductService,
    private toastr: ToastrService){

    }

    onApprove(status: string){
      this.productService.updateApprovalStatus(this.data.productId, this.data.version, status).subscribe(
        data => {
          console.log(data);
          if(status=='APPROVED'){
            this.toastr.success(this.data.productName+' phiên bản '+this.data.version,'Duyệt sản phẩm thành công')
          }else{
            this.toastr.success(this.data.productName+' phiên bản '+this.data.version,'Từ chối sản phẩm thành công')
          }
          this.dialogRef.close({data: "done"});
        },
        error => {
          this.toastr.error("Đã có lỗi xảy ra");
          this.dialogRef.close({data: "error"});
        }
      )
    }
}
