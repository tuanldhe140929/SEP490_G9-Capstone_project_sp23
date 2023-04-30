import { Component, Inject, Input, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { AddviolationService } from 'src/app/services/addviolation.service';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-addviolation',
  templateUrl: './addviolation.component.html',
  styleUrls: ['./addviolation.component.css']
})
export class AddviolationComponent implements OnInit{

  constructor(private formBuilder: FormBuilder, private addviolationService: AddviolationService,private vioService: AddviolationService, private toastr: ToastrService, @Inject(MAT_DIALOG_DATA) public data: { account_id: number, email: string, enabled: boolean}) { }
  account_id: number;

      ngOnInit(): void {
      console.log(this.account_id)
      }

  addViolationForm = this.formBuilder.group({
    "description": ['', [Validators.required]]
  });

  get form() {
    return this.addViolationForm.controls;
  }

  get lengthform() {
    return this.addViolationForm.controls.description.value?.length;

  }

  get description() {
    return this.addViolationForm.controls.description;
  }

  addViolation() {
    if (this.addViolationForm.valid) {
        this.account_id = this.data.account_id;
        this.addviolationService.addViolation(this.account_id,""+this.addViolationForm.controls.description.value).subscribe(
          data => {
            console.log(data);
            this.toastr.success('Thêm vi phạm thành công');
          }
        )
        this.vioService.updateSellerStatus(this.data.account_id).subscribe(
          data => {
            console.log(data);      
              this.toastr.success("Đã cấm tài khoản thành công!");       
          }
        )
    }
  }

}







