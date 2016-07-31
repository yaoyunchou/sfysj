/**
 * Created by yao on 2016/6/22.
 */
describe("A suite of basic functions", function() {
	it("reverse word",function(){
		expect("DCBA").toEqual(reverse("ABCD"));
		expect("Conan").toEqual(reverse("nano"));
	});
});
