import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
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

  constructor(private formBuilder: FormBuilder, private addviolationService: AddviolationService, private toastr: ToastrService, @Inject(MAT_DIALOG_DATA) public data: { descriptionList: string[]; }) { }
  ngOnInit(): void {
    
  }

  addViolationForm = this.formBuilder.group({
    "description": ['', [Validators.required]]
  });

  get form() {
    return this.addViolationForm;
  }

  get description() {
    return this.addViolationForm.controls.description;
  }


  checkDescriptionExists() {
    if (this.description.value) {
      return true;
    }
    return false;
  }

  addViolation() {
    if (this.addViolationForm.valid) {
      if (this.checkDescriptionExists()) {
        this.addviolationService.addViolation(this.addViolationForm.value).subscribe(
          data => {
            console.log(data);
            this.toastr.success('Thêm chủ đề thành công');
          }
        );
      } else {
        this.toastr.error('Vui lòng sử dụng tên khác', 'Tên chủ đề đã được sử dụng');
      }
    }
  }

}







