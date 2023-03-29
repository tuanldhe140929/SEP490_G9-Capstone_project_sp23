import { Component, Inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { CategoryService } from 'src/app/services/category.service';

@Component({
  selector: 'app-add-category',
  templateUrl: './add-category.component.html',
  styleUrls: ['./add-category.component.css']
})
export class AddCategoryComponent {

  categoryName: string = "";

  constructor(private formBuilder: FormBuilder, private categoryService: CategoryService, private toastr: ToastrService, @Inject(MAT_DIALOG_DATA) public data: {nameList: string[]}){}

  addCategoryForm = this.formBuilder.group({
    "name": ['',[Validators.required]]
  });

  get form(){
    return this.addCategoryForm;
  }

  get name(){
    return this.addCategoryForm.controls.name;
  }

  checkNameExists(){
    console.log(this.categoryName);
    for(let i=0;i<this.data.nameList.length;i++){
      if(this.name.value?.toLowerCase().trim()==this.data.nameList[i].toLowerCase().trim()){
        return true;
      }
    }
    return false;
  }

  addCategory(){
    if(this.addCategoryForm.valid){
      if(!this.checkNameExists()){
        this.categoryService.addCategory(this.addCategoryForm.value).subscribe(
          data => {
            console.log(data);
            this.toastr.success('Thêm phân loại thành công')
          }
        )
      }else{
        this.toastr.error('Vui lòng sử dụng tên khác','Tên phân loại đã được sử dụng')
      }
    }
  }
  
}
