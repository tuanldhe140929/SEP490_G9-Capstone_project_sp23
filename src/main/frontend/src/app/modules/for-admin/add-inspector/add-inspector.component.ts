import { HttpClient } from '@angular/common/http';
import { Component, OnInit} from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { BehaviorSubject, Observable } from 'rxjs';
import { ForAdminService } from 'src/app/services/for-admin.service';

@Component({
  selector: 'app-add-inspector',
  templateUrl: './add-inspector.component.html',
  styleUrls: ['./add-inspector.component.css']
})
export class AddInspectorComponent implements OnInit{
  constructor(private formBuilder: FormBuilder, private httpClient: HttpClient, private forAdminService: ForAdminService, private toastr: ToastrService, private router: Router){}

  ngOnInit(): void {
    
  }

  get form(){
    return this.addInspectorForm.controls;
  }

  get password(){
    return this.addInspectorForm.controls.password;
  }

  get repassword(){
    return this.addInspectorForm.controls.repassword;
  }

  onPasswordMatch(): boolean{
    if(this.password.value==this.repassword.value){
      return true;
    }else{
      return false;
    }
  }

  addInspectorForm = this.formBuilder.group({
    "email": ['',[Validators.required,Validators.email]],
    "username": ['',[Validators.required,Validators.minLength(4),Validators.maxLength(10), Validators.pattern("^[a-zA-Z0-9_]*$")]],
    "password": ['',[Validators.required,Validators.minLength(8),Validators.maxLength(30)]],
    "repassword": ['',[Validators.required,Validators.minLength(8),Validators.maxLength(30)]]
  })

  public noWhitespaceValidator(control: FormControl) {
    const isWhitespace = (control.value || '').trim().includes(' ');
    const isValid = !isWhitespace;
    return isValid ? null : { 'whitespace': true };
  }

  data: any;


  // public onAddInspector() {
  //   if (this.addInspectorForm.valid) {
  //     this.forAdminService.addInspector(this.addInspectorForm.value).subscribe(
  //       data => {
  //         console.log(data);
  //         this.router.navigate(['/inspectorList']);
  //         this.toastr.success('Inspector added successfully!');
  //       },
  //     );
  //   }
  // }
}
