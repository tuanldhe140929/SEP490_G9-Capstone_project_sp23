import { Component, Inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { ForAdminService } from 'src/app/services/for-admin.service';

@Component({
  selector: 'app-update-tag',
  templateUrl: './update-tag.component.html',
  styleUrls: ['./update-tag.component.css']
})
export class UpdateTagComponent {
  constructor(private formBuilder: FormBuilder, private forAdminService: ForAdminService, private toastr: ToastrService, @Inject(MAT_DIALOG_DATA) public data: {nameList: string[], id: number, oldName: string}){}

  ngOnInit(): void {
    this.updateTagForm.controls['name'].setValue(this.data.oldName)
  }

  updateTagForm = this.formBuilder.group({
    "name": ['',[Validators.required]]
  })

  checkNameExists(){
    if(this.name.value?.toLowerCase().trim()!=this.data.oldName.toLowerCase().trim()){
      for(let i=0;i<this.data.nameList.length;i++){
        if(this.name.value?.toLowerCase().trim()==this.data.nameList[i].toLowerCase().trim()){
          return true;
        }
      }
      return false;
    }else{
      return false;
    }
  }

  get form(){
    return this.updateTagForm;
  }

  get name(){
    return this.updateTagForm.controls.name;
  }

  updateTag(id: number){
    if(this.updateTagForm.valid){
      if(!this.checkNameExists()){
        this.forAdminService.updateTag(this.updateTagForm.value,id).subscribe(
          response => {
            console.log(response);
            this.toastr.success('Cập nhật tên chủ đề thành công!')
          }
        )
      }else{
        this.toastr.error('Tên đã được sử dụng')
      }
      
    }
    
  }
}
