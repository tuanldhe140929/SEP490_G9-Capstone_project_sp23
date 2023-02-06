import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { ManageInspectorService } from 'src/app/services/manage-inspector.service';

@Component({
  selector: 'app-add-inspector',
  templateUrl: './add-inspector.component.html',
  styleUrls: ['./add-inspector.component.css']
})
export class AddInspectorComponent implements OnInit{

  constructor(private httpClient: HttpClient, private formBuilder: FormBuilder, private manageInspectorService: ManageInspectorService){}

  addInspectorForm = this.formBuilder.group({
    "email": ['',[Validators.required, Validators.email]],
    "username": ['',[Validators.required, Validators.minLength(3), Validators.maxLength(10)]],
    "password": ['',[Validators.required, Validators.minLength(8), Validators.maxLength(30)]],
    "repassword": ['',[Validators.required, Validators.minLength(8), Validators.maxLength(30)]]
  });

  getEmail(){
    return this.addInspectorForm.controls.email;
  }

  ngOnInit(): void {
    
  }

  public onAddInspector() {
    if (this.addInspectorForm.valid) {
      
      this.manageInspectorService.addInspector(this.addInspectorForm.value).subscribe(
        data => {
          console.log(data)
        },

      );
    }
  }
}
