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

  constructor(private formBuilder: FormBuilder, private addviolationService: AddviolationService, private toastr: ToastrService, @Inject(MAT_DIALOG_DATA) public data: { account_id: number}) { }
  
  // data: violation;
  
  violation_id: number;
  createdDate: Date;
  account_id: number;
  description: string;
      ngOnInit(): void {
        console.log(this.data.account_id);
        // this.description = "dfaf";

      }

  addViolationForm = this.formBuilder.group({
    description: ['', [Validators.required]],
  });

  get form() {
    return this.addViolationForm;
  }

  // get description() {
  //   return this.addViolationForm.controls.description;
  // }


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
     console.log(this.violation_id,this.createdDate,this.description,this.account_id);
        // this.description = this.addViolationForm.controls.description.value;
        this.addviolationService.sendViolation(this.violation_id,this.createdDate,'hhhhh',this.account_id).subscribe(
          data => {
            console.log(data);
            // this.toastr.success('Thêm chủ đề thành công');
          }
        );
      // } else {
      //   this.toastr.error('Vui lòng sử dụng tên khác', 'Tên chủ đề đã được sử dụng');
      // }
    }
  }

}







