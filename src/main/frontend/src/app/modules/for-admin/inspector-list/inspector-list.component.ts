import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { ForAdminService } from 'src/app/services/for-admin.service';
import { AddInspectorComponent } from '../add-inspector/add-inspector.component';


@Component({
  selector: 'app-inspector-list',
  templateUrl: './inspector-list.component.html',
  styleUrls: ['./inspector-list.component.css']
})

export class InspectorListComponent implements OnInit{

  constructor(private forAdminService: ForAdminService, private httpClient: HttpClient, private dialog: MatDialog){}

  inspectorList: any;

  ngOnInit(): void {
    this.getAllInspectors(); 
  }

  public getAllInspectors(): void{
    this.forAdminService.getAllInspectors().subscribe(
      response => {
        this.inspectorList = response;
      }
    )
  }

  openDialog(){
    this.dialog.open(AddInspectorComponent, {width: '70%'});
  }
}

