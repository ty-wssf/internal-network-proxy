import { applyPureVueInReact, VueContainer, getVNode, getReactNode } from 'veaury'
import { useRef } from 'react'
import { h } from 'vue'
import AAVue from './AA.vue'
const AA = applyPureVueInReact(AAVue)

export default function () {
  const vSlots = useRef({
    // The scoped slot of the vue component AA
    // The v-slots property will automatically convert reactNode to VNode, so there is no need to manually use getVNode for conversion.
    renderSomething(VNode) {
      return <>
        <h4>Render scoped slots</h4>
        {/* There are two ways to consume VNode in reactNode. */}
        <div style={{background: 'green', color: 'white'}} data-testid="VueContainerTest">
          <span>rendered with VueContainer</span>
          {/* The first way is to use VueContainer */}
          <VueContainer node={VNode}/>
        </div>
        <div style={{background: 'seagreen', color: 'white', marginTop: '5px'}}>
          <span>rendered with getReactNode</span>
          {/* The second way is to directly convert VNode to ReactNode. */}
          {getReactNode(VNode)}
        </div>
      </>
    }
  })
  // `VNodeBar` is a property of type VNode, so use getVNode to convert reactNode to VNode.
  const VNodeBar = useRef(getVNode(
    <div style={{background: '#105a31', marginTop: '5px', color: 'white'}}>
      <div>rendered with a property</div>
      <div>This is Bar's VNode</div>
    </div>
  ))
  const VNode = h('div', null, 'simple vnode')
  return <>
    <h2>This example shows how to transform and render directly in reactNode and VNode.</h2>
    <AA v-slots={vSlots.current} VNodeBar={VNodeBar.current}/>
    {/* Just for testing */}
    <VueContainer node={VNode}/>
    {/* Just for testing */}
    <VueContainer/>
    {/* Just for testing */}
    <VueContainer node={1212}/>
    {/* Just for testing */}
    <VueContainer node={() => 4444}/>
  </>
}
