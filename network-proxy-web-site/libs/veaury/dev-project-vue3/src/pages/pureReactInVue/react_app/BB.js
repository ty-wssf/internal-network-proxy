import React from 'react'

export default function BB(props) {
  return <>
    {props.children}
    {props.renderProps?.()}
    {props.reactNode}
  </>
}
