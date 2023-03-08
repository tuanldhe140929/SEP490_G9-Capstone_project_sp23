import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort, Sort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import { Tag } from 'src/app/DTOS/Tag';
import { TagService } from 'src/app/services/tag.service';
import { AddTagComponent } from './add-tag/add-tag.component';
import { UpdateTagComponent } from './update-tag/update-tag.component';

@Component({
  selector: 'app-tags',
  templateUrl: './tags.component.html',
  styleUrls: ['./tags.component.css']
})
export class TagsComponent implements AfterViewInit{
  displayedColumns: string[] = ['ID', 'Tên chủ đề','Hành động'];
  dataSource: MatTableDataSource<Tag>;

  tagList: Tag[] = [];
  nameList: string[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private tagService: TagService, private dialog: MatDialog) {    
    this.tagService.getAllTags().subscribe(response => {
      this.dataSource = new MatTableDataSource(response);
      this.tagList = response;
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
      for(let i=0;i<this.tagList.length;i++){
        this.nameList.push(this.tagList[i].name);
      }
      console.log(this.nameList.length)
    })

    // Assign the data to the data source for the table to render
    // this.dataSource = new MatTableDataSource([{'id':1,'name':'AAA'},{'id':2,'name':'BBB'}]);
  }


  ngAfterViewInit(): void {
    
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  openAddDialog(getNameList: string[]) {
    const dialogRef = this.dialog.open(AddTagComponent, {
      data: {
        nameList: getNameList
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
      setTimeout(() => this.refresh(),400)
    });
  }

  openUpdateDialog(getNameList: string[], getId: number, getOldName: string) {
    const dialogRef = this.dialog.open(UpdateTagComponent, {
      data: {
        nameList: getNameList,
        id: getId,
        oldName: getOldName
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
      setTimeout(() => this.refresh(),400)
    });
  }

  refresh() {
    this.tagService.getAllTags().subscribe((data: Tag[]) => {
      this.dataSource.data = data;
      this.nameList = [];
      for (let i = 0; i < data.length; i++) {
        this.nameList.push(data[i].name);
      }
      console.log(data);
    })
  }
}


