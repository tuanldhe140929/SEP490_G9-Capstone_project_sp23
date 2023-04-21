import { Component, Inject, Input, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { AddviolationService } from 'src/app/services/addviolation.service';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { violation } from 'src/app/DTOS/Violation';

@Component({
  selector: 'app-addviolation',
  templateUrl: './addviolation.component.html',
  styleUrls: ['./addviolation.component.css']
})
export class AddviolationComponent implements OnInit{

  constructor(private formBuilder: FormBuilder, private addviolationService: AddviolationService,private vioService: AddviolationService, private toastr: ToastrService, @Inject(MAT_DIALOG_DATA) public data: { account_id: number, email: string, enabled: boolean}) { }
  
  account_id: number;
  // formerStatus: boolean = this.data.enabled;

      ngOnInit(): void {
      console.log(this.account_id)
      }

  addViolationForm = this.formBuilder.group({
    "description": ['', [Validators.required]],
  });

  get form() {
    return this.addViolationForm;
  }

  get description() {
    return this.addViolationForm.controls.description;
  }


  // checkDescriptionExists() {
  //   if (this.description) {
  //     return true;
  //   }
  //   return false;
  // }

  addViolation() {
    if (this.addViolationForm.valid) {
      // if (this.checkDescriptionExists()) {
      
        this.account_id = this.data.account_id;
    //  console.log(this.violation_id,this.createdDate,this.description,this.account_id);
        // this.description = this.addViolationForm.controls.description.value;
        this.addviolationService.addViolation(this.account_id,"description").subscribe(
          data => {
            console.log(data);
            this.toastr.success('Thêm vi phạm thành công');
          }
        )
        this.vioService.updateSellerStatus(this.data.account_id).subscribe(
          data => {
            console.log(data);
            // if(this.formerStatus){
              this.toastr.success("Đã cấm tài khoản thành công!");
            // }else{
            //   this.toastr.success("Bật tài khoản thành công!");
            // }
            
          }
        )

        // this.formerStatus = false;
        // this.addviolationService.updateSellerStatus(this.account_id).subscribe(
        //   data => {
        //     console.log(data);
        //     this.toastr.success('Đã tiến hành cấm người bán vi phạm');
        //   }
        // )
      // } else {
      //   this.toastr.error('Vui lòng sử dụng tên khác', 'Tên chủ đề đã được sử dụng');
      // }
    }
  }

}







