	export class ProductFile{
		id:number;
		name:string;
		type:string;
		size:number;
		displayOrder:number;
		lastUpdate:Date;
		
		constructor(){
			this.id=-1;
			this.name="";
			this.type="";
			this.size=-1;
			this.displayOrder = -1;
			this.lastUpdate = new Date();
		}
	}