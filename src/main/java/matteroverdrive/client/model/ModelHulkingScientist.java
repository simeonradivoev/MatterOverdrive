/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelHulkingScientist extends ModelBiped
{
	//fields
	final ModelRenderer head;
	final ModelRenderer body;
	final ModelRenderer rightArm;
	final ModelRenderer leftArm;
	final ModelRenderer rightLeg;
	final ModelRenderer leftLeg;
	final ModelRenderer lowerRightLeg;
	final ModelRenderer lowerLeftLeg;
	final ModelRenderer lowerBody;
	final ModelRenderer lowerRightArm;
	final ModelRenderer lowerLeftArm;
	final ModelRenderer lowerJaw;
	final ModelRenderer nose;

	public ModelHulkingScientist()
	{
		super(2, 2, 64, 64);

		float expand = 0;
		//region body
		lowerBody = new ModelRenderer(this, 40, 0);
		lowerBody.addBox(-4, -7, -2, 8, 9, 4, expand);
		lowerBody.setRotationPoint(0F, 11, 0F);
		lowerBody.setTextureSize(64, 64);
		lowerBody.mirror = true;
		setRotation(lowerBody, 0.0F, 0F, 0F);
		body = new ModelRenderer(this, 0, 48);
		body.addBox(-6F, -7F, -3F, 12, 9, 7, expand);
		body.setRotationPoint(0F, -7, -2);
		body.setTextureSize(64, 64);
		body.mirror = true;
		setRotation(body, 0.6723132F, 0F, 0F);
		lowerBody.addChild(body);
		//endregion
		//region Head
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-3F, -8F, -4F, 6, 8, 6, expand);
		head.setRotationPoint(0F, -6F, -1F);
		head.setTextureSize(64, 64);
		head.mirror = true;
		setRotation(head, 0F, 0F, 0F);
		body.addChild(head);
		lowerJaw = new ModelRenderer(this, 0, 14);
		lowerJaw.addBox(-4F, 0F, 0F, 8, 3, 4, expand);
		lowerJaw.setRotationPoint(0F, -2F, -5.5F);
		lowerJaw.setTextureSize(64, 64);
		head.addChild(lowerJaw);
		nose = new ModelRenderer(this, 24, 0);
		nose.addBox(-1f, -5f, 0, 2, 3, 1, expand);
		nose.setRotationPoint(0F, 0, -5F);
		nose.setTextureSize(64, 64);
		head.addChild(nose);
		//endregion
		//region Right Arm
		rightArm = new ModelRenderer(this, 0, 35);
		rightArm.addBox(-3F, -2F, -2F, 4, 10, 3, expand);
		rightArm.setRotationPoint(-7F, -4F, 1F);
		rightArm.setTextureSize(64, 64);
		rightArm.mirror = true;
		setRotation(rightArm, -0.0436332F, 0F, 0.3351032F);
		body.addChild(rightArm);
		lowerRightArm = new ModelRenderer(this, 38, 51);
		lowerRightArm.addBox(-1.5f, 0, -2, 4, 10, 3, expand);
		lowerRightArm.setRotationPoint(-1.5f, 7F, 0.5f);
		lowerRightArm.setTextureSize(64, 64);
		lowerRightArm.mirror = true;
		setRotation(lowerRightArm, -1.041993F, 0F, 0);
		rightArm.addChild(lowerRightArm);
		//endregion
		//region Left Arm
		leftArm = new ModelRenderer(this, 14, 35);
		leftArm.addBox(-1F, -2F, -2F, 4, 10, 3, expand);
		leftArm.setRotationPoint(7F, -4F, 1F);
		leftArm.setTextureSize(64, 64);
		leftArm.mirror = true;
		body.addChild(leftArm);
		setRotation(leftArm, -0.0436332F, 0F, -0.3351032F);
		lowerLeftArm = new ModelRenderer(this, 38, 38);
		lowerLeftArm.addBox(-1.5f, 0, -2, 4, 10, 3, expand);
		lowerLeftArm.setRotationPoint(0.5f, 7F, 0.5f);
		lowerLeftArm.setTextureSize(64, 64);
		lowerLeftArm.mirror = true;
		setRotation(lowerLeftArm, -1.041996F, 0F, 0);
		leftArm.addChild(lowerLeftArm);
		//endregion
		//region Right Leg
		rightLeg = new ModelRenderer(this, 16, 21);
		rightLeg.addBox(-2F, 0F, -2F, 4, 10, 4, expand);
		rightLeg.setRotationPoint(-3F, 11F, 1F);
		rightLeg.setTextureSize(64, 64);
		rightLeg.mirror = true;
		setRotation(rightLeg, -0.9f, 0.418879F, 0F);
		lowerRightLeg = new ModelRenderer(this, 48, 26);
		lowerRightLeg.addBox(-1.5f, 0, 0, 4, 8, 4, expand);
		lowerRightLeg.setRotationPoint(-0.5f, 10, -2F);
		lowerRightLeg.setTextureSize(64, 64);
		lowerRightLeg.mirror = true;
		setRotation(lowerRightLeg, 1, 0, 0F);
		rightLeg.addChild(lowerRightLeg);
		//endregion
		//region Left Leg
		leftLeg = new ModelRenderer(this, 0, 21);
		leftLeg.addBox(-2F, 0F, -2F, 4, 10, 4, expand);
		leftLeg.setRotationPoint(3F, 11F, 1F);
		leftLeg.setTextureSize(64, 64);
		leftLeg.mirror = true;
		setRotation(leftLeg, -0.7f, -0.418879F, 0F);
		lowerLeftLeg = new ModelRenderer(this, 32, 26);
		lowerLeftLeg.addBox(-1.5f, 0, 0, 4, 8, 4, expand);
		lowerLeftLeg.setRotationPoint(-0.5F, 10F, -2F);
		lowerLeftLeg.setTextureSize(64, 64);
		lowerLeftLeg.mirror = true;
		setRotation(lowerLeftLeg, 1, 0, 0F);
		leftLeg.addChild(lowerLeftLeg);
		//endregion
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		//head.render(f5);
		//body.render(f5);
		//rightArm.render(f5);
		//leftArm.render(f5);
		rightLeg.render(f5);
		leftLeg.render(f5);
		lowerBody.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float yaw, float yawHead, float pitch, float f5, Entity entity)
	{
		float mainLimbSwing = limbSwing * 0.6662F;

		//super.setRotationAngles(f, f1, f2, f3, f4, f5,entity);
		body.rotateAngleX = MathHelper.cos(mainLimbSwing + (float)Math.PI) * 1.6F * limbSwingAmount * 0.3F + 0.5f;
		lowerBody.rotateAngleX = MathHelper.sin(mainLimbSwing) * 1.6F * limbSwingAmount * 0.3F - 0.1f;
		lowerBody.rotationPointY = MathHelper.sin(mainLimbSwing) * 1.6F * limbSwingAmount * 3F + 11;

		head.rotateAngleY = yawHead / (180F / (float)Math.PI);
		head.rotateAngleX = pitch / (180F / (float)Math.PI) - 0.3f;

		rightArm.rotateAngleX = MathHelper.cos(mainLimbSwing + (float)Math.PI / 2) * 1.6F * limbSwingAmount * 0.8F;
		leftArm.rotateAngleX = MathHelper.cos(mainLimbSwing) * 1.6F * limbSwingAmount * 0.8F;

		lowerRightArm.rotateAngleX = MathHelper.cos(mainLimbSwing + (float)Math.PI) * 0.2F * limbSwingAmount - 1;
		lowerLeftArm.rotateAngleX = MathHelper.cos(mainLimbSwing) * 0.2F * limbSwingAmount - 1;

		rightLeg.rotateAngleX = MathHelper.cos(mainLimbSwing) * 0.7F * limbSwingAmount - 0.7f;
		rightLeg.rotationPointY = MathHelper.sin(mainLimbSwing) * 1.4F * limbSwingAmount * 3 + 11;

		leftLeg.rotateAngleX = MathHelper.cos(mainLimbSwing + (float)Math.PI) * 0.7F * limbSwingAmount - 0.7f;
		leftLeg.rotationPointY = MathHelper.sin(mainLimbSwing) * 1.4F * limbSwingAmount * 3 + 11;

		lowerRightLeg.rotateAngleX = MathHelper.cos(mainLimbSwing) * 0.8F * limbSwingAmount + 1f;
		lowerLeftLeg.rotateAngleX = MathHelper.cos(mainLimbSwing + (float)Math.PI) * 0.8f * limbSwingAmount + 1f;

		body.rotateAngleY = MathHelper.sin(mainLimbSwing) * 0.2F * limbSwingAmount;
	}

}
