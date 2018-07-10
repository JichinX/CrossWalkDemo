import React, { Component, Text } from "react";
import "./App.css";
import ProtoColTest from "./component/Protocol/ProtocolTest";
import SupportTest from "./component/Support/SupportTest";
class App extends Component {
  render() {
    return (
      <div className="App">
        <p>CrossWalk测试Demo</p>
        <ProtoColTest />
        <SupportTest />
      </div>
    );
  }
}
export default App;
