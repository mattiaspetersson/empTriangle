EMPTriangle {
	var rotation, invert;
	var <triangle, rowCol, m = 1, a = 0;

	*new {|rotation = 0, invert = false|
		^super.newCopyArgs(rotation, invert).init;
	}

	init {
		var t = [
			[1, 2, 3, 4, 5, 6, 7, 8],
			[3, 5, 7, 9, 2, 4, 6],
			[8, 3, 7, 2, 6, 1],
			[2, 1, 9, 8, 7],
			[3, 1, 8, 6],
			[4, 9, 5],
			[4, 5],
			[9]
		];
		if(invert){t = t.reverse};
		triangle = ((t * m) + a);
		switch(rotation,
			0, { triangle = triangle },

			1, {
				triangle = this.reversedRows.flop.do{|a, i| if(i>0) {i.do{a.pop}}}; // 1
			},

			2, {
				triangle = this.triangle.reverse.flop.do{|a, i| if(i>0) {i.do{a.removeAt(0)}}}; // 2
			},

			-1, {
				triangle = this.triangle.flop.do{|a, i| if(i>0) {i.do{a.pop}}}; // -1
			},

			-2, {
				triangle = this.reversedRows.reverse.flop.do{|a, i| if(i>0) {i.do{a.removeAt(0)}}}; // -2
			}
		);
		rowCol = [];
		triangle.do{|r, y|
			r.do{|c, x|
				rowCol = rowCol.add([y, x]);
			};
		};
	}

	mulAdd {|mul = 1, add = 0|
		m = mul;
		a = add;
		this.init; // needs fixing with newCopyArgs
	}

	row {|num|
		^triangle[num];
	}

	reversedRow {|num, mode = 0|
		case {mode == 0} {^triangle.reverse[num]}; // Ran and Goldberg15 uses this!
		case {mode == 1} {^triangle[num].reverse};
	}

	reversedRows {|num, mode = 1|
		^8.collect{|i| this.reversedRow(i, mode)}
	}

	index {|i|
		^triangle.flatten[i];
	}

	reversedIndex {|i|
		^triangle.flatten.reverse[i];
	}

	indicesOf {|num|
		var a = [];
		triangle.flatten.do{|x, i|
			if(num == x, {a = a.add(i)});
		};
		^a;
	}

	reversedIndicesOf {|num|
		var a = [];
		triangle.flatten.reverse.do{|x, i|
			if(num == x, {a = a.add(i)});
		};
		^a;
	}

	patternOf {|num = 9|
		^triangle.flatten.collect{|n|
			if(n == num, {n}, {0});
		};
	}

	indexAsRowCol {|i|
		^rowCol[i];
	}

	rowAsDurations {|rowNum, timeUnit = 0.125|
		^this.row(rowNum) * timeUnit;
	}

	reversedRowAsDurations {|rowNum, timeUnit = 0.125|
		^this.reversedRow(rowNum) * timeUnit;
	}

	rowAsBooleanPattern {|rowNum|
		var a;
		a = this.row(rowNum).collect{|e|
			e.collect{|i| if(i==0, true, false)};
		};
		^a.flatten;
	}

	reversedRowAsBooleanPattern {|rowNum|
		var a;
		a = this.reversedRow(rowNum).collect{|e|
			e.collect{|i| if(i==0, true, false)};
		};
		^a.flatten;
	}

	rowAsTremolo {|rowNum, root = 0, numTrems = 4|
		var a = [];
		this.row(rowNum).collect{|e|
			numTrems.do{
				a = a.add(root);
				a = a.add(e);
			};
		};
		^a;
	}

	reversedRowAsTremolo {|rowNum, root = 0, numTrems = 4|
		var a = [];
		this.reversedRow(rowNum).collect{|e|
			numTrems.do{
				a = a.add(root);
				a = a.add(e);
			};
		};
		^a;
	}
}

EMPTriangleNumberView : SCViewHolder {
	var <objectText, <font, circleBackground, stringColor, string;

	*new {|parent, bounds|
		^super.new.init(parent, bounds);
	}

	init {|parent, bounds|
		this.view = UserView(parent, bounds)
		.minSize_(bounds.width@bounds.height)
		.background_(Color.clear);
		this.view.drawFunc = {
			Pen.fillColor = circleBackground;
			Pen.addOval(bounds);
			Pen.fill;
		};
		font = Font("Arial", 18);
		objectText = StaticText(view, bounds)
		.background_(Color.clear)
		.font_(font)
		.stringColor_(stringColor)
		.align_(\center)
		.string_(string);
		this.circleBackground(Color.grey);
		this.stringColor(Color.white);
		this.string("9");
		this.view.refresh;
	}

	circleBackground {|color|
		circleBackground = color;
		defer{this.view.refresh};
	}

	stringColor {|color|
		stringColor = color;
		objectText.stringColor_(stringColor);
	}

	string {|text|
		string = text;
		objectText.string_(string);
	}
}