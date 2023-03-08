import { Component, Inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { TagService } from 'src/app/services/tag.service';

@Component({
  selector: 'app-add-tag',
  templateUrl: './add-tag.component.html',
  styleUrls: ['./add-tag.component.css']
})
export class AddTagComponent {
  constructor(private formBuilder: FormBuilder, private tagService: TagService, private toastr: ToastrService, @Inject(MAT_DIALOG_DATA) public data: {nameList: string[]}){}

  addTagForm = this.formBuilder.group({
    "name": ['',[Validators.required]]
  });

  get form(){
    return this.addTagForm;
  }

  get name(){
    return this.addTagForm.controls.name;
  }

  checkNameExists(){
    for(let i=0;i<this.data.nameList.length;i++){
      if(this.name.value?.toLowerCase().trim()==this.data.nameList[i].toLowerCase().trim()){
        return true;
      }
    }
    return false;
  }

  addCategory(){
    if(this.addTagForm.valid){
      if(!this.checkNameExists()){
        this.tagService.addTag(this.addTagForm.value).subscribe(
          data => {
            console.log(data);
            this.toastr.success('Thêm chủ đề thành công')
          }
        )
      }else{
        this.toastr.error('Vui lòng sử dụng tên khác','Tên chủ đề đã được sử dụng')
      }
    }
  }
}
